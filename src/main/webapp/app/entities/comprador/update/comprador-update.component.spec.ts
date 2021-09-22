jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { CompradorService } from '../service/comprador.service';
import { IComprador, Comprador } from '../comprador.model';

import { CompradorUpdateComponent } from './comprador-update.component';

describe('Component Tests', () => {
  describe('Comprador Management Update Component', () => {
    let comp: CompradorUpdateComponent;
    let fixture: ComponentFixture<CompradorUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let compradorService: CompradorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CompradorUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(CompradorUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CompradorUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      compradorService = TestBed.inject(CompradorService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const comprador: IComprador = { id: 456 };

        activatedRoute.data = of({ comprador });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(comprador));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Comprador>>();
        const comprador = { id: 123 };
        jest.spyOn(compradorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ comprador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comprador }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(compradorService.update).toHaveBeenCalledWith(comprador);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Comprador>>();
        const comprador = new Comprador();
        jest.spyOn(compradorService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ comprador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: comprador }));
        saveSubject.complete();

        // THEN
        expect(compradorService.create).toHaveBeenCalledWith(comprador);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Comprador>>();
        const comprador = { id: 123 };
        jest.spyOn(compradorService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ comprador });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(compradorService.update).toHaveBeenCalledWith(comprador);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
