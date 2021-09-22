jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CocheService } from '../service/coche.service';
import { ICoche, Coche } from '../coche.model';
import { IVenta } from 'app/entities/venta/venta.model';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { CocheUpdateComponent } from './coche-update.component';

describe('Component Tests', () => {
  describe('Coche Management Update Component', () => {
    let comp: CocheUpdateComponent;
    let fixture: ComponentFixture<CocheUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let cocheService: CocheService;
    let ventaService: VentaService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CocheUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CocheUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CocheUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      cocheService = TestBed.inject(CocheService);
      ventaService = TestBed.inject(VentaService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Venta query and add missing value', () => {
        const coche: ICoche = { id: 456 };
        const venta: IVenta = { id: 57015 };
        coche.venta = venta;

        const ventaCollection: IVenta[] = [{ id: 25796 }];
        jest.spyOn(ventaService, 'query').mockReturnValue(of(new HttpResponse({ body: ventaCollection })));
        const additionalVentas = [venta];
        const expectedCollection: IVenta[] = [...additionalVentas, ...ventaCollection];
        jest.spyOn(ventaService, 'addVentaToCollectionIfMissing').mockReturnValue(expectedCollection);

        activatedRoute.data = of({ coche });
        comp.ngOnInit();

        expect(ventaService.query).toHaveBeenCalled();
        expect(ventaService.addVentaToCollectionIfMissing).toHaveBeenCalledWith(ventaCollection, ...additionalVentas);
        expect(comp.ventasSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const coche: ICoche = { id: 456 };
        const venta: IVenta = { id: 4937 };
        coche.venta = venta;

        activatedRoute.data = of({ coche });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(coche));
        expect(comp.ventasSharedCollection).toContain(venta);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Coche>>();
        const coche = { id: 123 };
        jest.spyOn(cocheService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ coche });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: coche }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(cocheService.update).toHaveBeenCalledWith(coche);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Coche>>();
        const coche = new Coche();
        jest.spyOn(cocheService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ coche });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: coche }));
        saveSubject.complete();

        // THEN
        expect(cocheService.create).toHaveBeenCalledWith(coche);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Coche>>();
        const coche = { id: 123 };
        jest.spyOn(cocheService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ coche });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(cocheService.update).toHaveBeenCalledWith(coche);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackVentaById', () => {
        it('Should return tracked Venta primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackVentaById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });
  });
});
