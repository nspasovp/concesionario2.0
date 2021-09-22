import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IVendedor, Vendedor } from '../vendedor.model';

import { VendedorService } from './vendedor.service';

describe('Service Tests', () => {
  describe('Vendedor Service', () => {
    let service: VendedorService;
    let httpMock: HttpTestingController;
    let elemDefault: IVendedor;
    let expectedResult: IVendedor | IVendedor[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(VendedorService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        dni: 'AAAAAAA',
        nombre: 'AAAAAAA',
        primeriApellido: 'AAAAAAA',
        segundoApellido: 'AAAAAAA',
        fechaNacimiento: currentDate,
        fechaContratacion: currentDate,
        fechaBaja: currentDate,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            fechaNacimiento: currentDate.format(DATE_FORMAT),
            fechaContratacion: currentDate.format(DATE_FORMAT),
            fechaBaja: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Vendedor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            fechaNacimiento: currentDate.format(DATE_FORMAT),
            fechaContratacion: currentDate.format(DATE_FORMAT),
            fechaBaja: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
            fechaContratacion: currentDate,
            fechaBaja: currentDate,
          },
          returnedFromService
        );

        service.create(new Vendedor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Vendedor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dni: 'BBBBBB',
            nombre: 'BBBBBB',
            primeriApellido: 'BBBBBB',
            segundoApellido: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_FORMAT),
            fechaContratacion: currentDate.format(DATE_FORMAT),
            fechaBaja: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
            fechaContratacion: currentDate,
            fechaBaja: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Vendedor', () => {
        const patchObject = Object.assign(
          {
            nombre: 'BBBBBB',
            primeriApellido: 'BBBBBB',
            segundoApellido: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_FORMAT),
            fechaContratacion: currentDate.format(DATE_FORMAT),
          },
          new Vendedor()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
            fechaContratacion: currentDate,
            fechaBaja: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Vendedor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            dni: 'BBBBBB',
            nombre: 'BBBBBB',
            primeriApellido: 'BBBBBB',
            segundoApellido: 'BBBBBB',
            fechaNacimiento: currentDate.format(DATE_FORMAT),
            fechaContratacion: currentDate.format(DATE_FORMAT),
            fechaBaja: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            fechaNacimiento: currentDate,
            fechaContratacion: currentDate,
            fechaBaja: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Vendedor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addVendedorToCollectionIfMissing', () => {
        it('should add a Vendedor to an empty array', () => {
          const vendedor: IVendedor = { id: 123 };
          expectedResult = service.addVendedorToCollectionIfMissing([], vendedor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vendedor);
        });

        it('should not add a Vendedor to an array that contains it', () => {
          const vendedor: IVendedor = { id: 123 };
          const vendedorCollection: IVendedor[] = [
            {
              ...vendedor,
            },
            { id: 456 },
          ];
          expectedResult = service.addVendedorToCollectionIfMissing(vendedorCollection, vendedor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Vendedor to an array that doesn't contain it", () => {
          const vendedor: IVendedor = { id: 123 };
          const vendedorCollection: IVendedor[] = [{ id: 456 }];
          expectedResult = service.addVendedorToCollectionIfMissing(vendedorCollection, vendedor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vendedor);
        });

        it('should add only unique Vendedor to an array', () => {
          const vendedorArray: IVendedor[] = [{ id: 123 }, { id: 456 }, { id: 24947 }];
          const vendedorCollection: IVendedor[] = [{ id: 123 }];
          expectedResult = service.addVendedorToCollectionIfMissing(vendedorCollection, ...vendedorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const vendedor: IVendedor = { id: 123 };
          const vendedor2: IVendedor = { id: 456 };
          expectedResult = service.addVendedorToCollectionIfMissing([], vendedor, vendedor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(vendedor);
          expect(expectedResult).toContain(vendedor2);
        });

        it('should accept null and undefined values', () => {
          const vendedor: IVendedor = { id: 123 };
          expectedResult = service.addVendedorToCollectionIfMissing([], null, vendedor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(vendedor);
        });

        it('should return initial array if no Vendedor is added', () => {
          const vendedorCollection: IVendedor[] = [{ id: 123 }];
          expectedResult = service.addVendedorToCollectionIfMissing(vendedorCollection, undefined, null);
          expect(expectedResult).toEqual(vendedorCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
