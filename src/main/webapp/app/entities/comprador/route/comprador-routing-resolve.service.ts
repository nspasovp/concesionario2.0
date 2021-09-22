import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IComprador, Comprador } from '../comprador.model';
import { CompradorService } from '../service/comprador.service';

@Injectable({ providedIn: 'root' })
export class CompradorRoutingResolveService implements Resolve<IComprador> {
  constructor(protected service: CompradorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IComprador> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((comprador: HttpResponse<Comprador>) => {
          if (comprador.body) {
            return of(comprador.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Comprador());
  }
}
