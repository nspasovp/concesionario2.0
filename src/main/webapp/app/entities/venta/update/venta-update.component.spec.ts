jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VentaService } from '../service/venta.service';
import { IVenta, Venta } from '../venta.model';
import { IVendedor } from 'app/entities/vendedor/vendedor.model';
import { VendedorService } from 'app/entities/vendedor/service/vendedor.service';
import { IComprador } from 'app/entities/comprador/comprador.model';
import { CompradorService } from 'app/entities/comprador/service/comprador.service';

import { VentaUpdateComponent } from './venta-update.component';

describe('Component Tests', () => {
  describe('Venta Management Update Component', () => {
    let comp: VentaUpdateComponent;
    let fixture: ComponentFixture<VentaUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let ventaService: VentaService;
    let vendedorService: VendedorService;
    let compradorService: CompradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VentaUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VentaUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VentaUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      ventaService = TestBed.inject(VentaService);
      vendedorService = TestBed.inject(VendedorService);
      compradorService = TestBed.inject(CompradorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Vendedor query and add missing value', () => {
        const venta: IVenta = { id: 456 };
        const vendedor: IVendedor = { id: 68935 };
        venta.vendedor = vendedor;

        const vendedorCollection: IVendedor[] = [{ id: 59639 }];
        jest.spyOn(vendedorService, 'query').mockReturnValue(of(new HttpResponse({ body: vendedorCollection })));
        const additionalVendedors = [vendedor];
        const expectedCollection: IVendedor[] = [...additionalVendedors, ...vendedorCollection];
        jest.spyOn(vendedorService, 'addVendedorToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        expect(vendedorService.query).toHaveBeenCalled();
        expect(vendedorService.addVendedorToCollectionIfMissing).toHaveBeenCalledWith(vendedorCollection, ...additionalVendedors);
        expect(comp.vendedorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Comprador query and add missing value', () => {
        const venta: IVenta = { id: 456 };
        const comprador: IComprador = { id: 17705 };
        venta.comprador = comprador;

        const compradorCollection: IComprador[] = [{ id: 74503 }];
        jest.spyOn(compradorService, 'query').mockReturnValue(of(new HttpResponse({ body: compradorCollection })));
        const additionalCompradors = [comprador];
        const expectedCollection: IComprador[] = [...additionalCompradors, ...compradorCollection];
        jest.spyOn(compradorService, 'addCompradorToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        expect(compradorService.query).toHaveBeenCalled();
        expect(compradorService.addCompradorToCollectionIfMissing).toHaveBeenCalledWith(compradorCollection, ...additionalCompradors);
        expect(comp.compradorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const venta: IVenta = { id: 456 };
        const vendedor: IVendedor = { id: 92428 };
        venta.vendedor = vendedor;
        const comprador: IComprador = { id: 49710 };
        venta.comprador = comprador;

        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(venta));
        expect(comp.vendedorsSharedCollection).toContain(vendedor);
        expect(comp.compradorsSharedCollection).toContain(comprador);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Venta>>();
        const venta = { id: 123 };
        jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: venta }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(ventaService.update).toHaveBeenCalledWith(venta);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Venta>>();
        const venta = new Venta();
        jest.spyOn(ventaService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: venta }));
        saveSubject.complete();

        // THEN
        expect(ventaService.create).toHaveBeenCalledWith(venta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Venta>>();
        const venta = { id: 123 };
        jest.spyOn(ventaService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ venta });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(ventaService.update).toHaveBeenCalledWith(venta);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVendedorById', () => {
        it('Should return tracked Vendedor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVendedorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCompradorById', () => {
        it('Should return tracked Comprador primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCompradorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
