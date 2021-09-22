import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CompradorDetailComponent } from './comprador-detail.component';

describe('Component Tests', () => {
  describe('Comprador Management Detail Component', () => {
    let comp: CompradorDetailComponent;
    let fixture: ComponentFixture<CompradorDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [CompradorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ comprador: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(CompradorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(CompradorDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load comprador on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.comprador).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
