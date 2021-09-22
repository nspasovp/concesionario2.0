jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IVendedor, Vendedor } from '../vendedor.model';
import { VendedorService } from '../service/vendedor.service';

import { VendedorRoutingResolveService } from './vendedor-routing-resolve.service';

describe('Service Tests', () => {
  describe('Vendedor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: VendedorRoutingResolveService;
    let service: VendedorService;
    let resultVendedor: IVendedor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(VendedorRoutingResolveService);
      service = TestBed.inject(VendedorService);
      resultVendedor = undefined;
    });

    describe('resolve', () => {
      it('should return IVendedor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVendedor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVendedor).toEqual({ id: 123 });
      });

      it('should return new IVendedor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVendedor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultVendedor).toEqual(new Vendedor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Vendedor })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultVendedor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultVendedor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
