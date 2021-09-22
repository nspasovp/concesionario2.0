jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { VendedorService } from '../service/vendedor.service';
import { IVendedor, Vendedor } from '../vendedor.model';

import { VendedorUpdateComponent } from './vendedor-update.component';

describe('Component Tests', () => {
  describe('Vendedor Management Update Component', () => {
    let comp: VendedorUpdateComponent;
    let fixture: ComponentFixture<VendedorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let vendedorService: VendedorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [VendedorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(VendedorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(VendedorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      vendedorService = TestBed.inject(VendedorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const vendedor: IVendedor = { id: 456 };

        activatedRoute.data = of({ vendedor });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(vendedor));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vendedor>>();
        const vendedor = { id: 123 };
        jest.spyOn(vendedorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vendedor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vendedor }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(vendedorService.update).toHaveBeenCalledWith(vendedor);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vendedor>>();
        const vendedor = new Vendedor();
        jest.spyOn(vendedorService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vendedor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: vendedor }));
        saveSubject.complete();

        // THEN
        expect(vendedorService.create).toHaveBeenCalledWith(vendedor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Vendedor>>();
        const vendedor = { id: 123 };
        jest.spyOn(vendedorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ vendedor });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(vendedorService.update).toHaveBeenCalledWith(vendedor);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
