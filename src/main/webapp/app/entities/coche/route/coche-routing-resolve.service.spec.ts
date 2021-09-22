jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICoche, Coche } from '../coche.model';
import { CocheService } from '../service/coche.service';

import { CocheRoutingResolveService } from './coche-routing-resolve.service';

describe('Service Tests', () => {
  describe('Coche routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CocheRoutingResolveService;
    let service: CocheService;
    let resultCoche: ICoche | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CocheRoutingResolveService);
      service = TestBed.inject(CocheService);
      resultCoche = undefined;
    });

    describe('resolve', () => {
      it('should return ICoche returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoche = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCoche).toEqual({ id: 123 });
      });

      it('should return new ICoche if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoche = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCoche).toEqual(new Coche());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Coche })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoche = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCoche).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
