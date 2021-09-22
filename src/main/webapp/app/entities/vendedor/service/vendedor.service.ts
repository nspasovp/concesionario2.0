import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IVendedor, getVendedorIdentifier } from '../vendedor.model';

export type EntityResponseType = HttpResponse<IVendedor>;
export type EntityArrayResponseType = HttpResponse<IVendedor[]>;

@Injectable({ providedIn: 'root' })
export class VendedorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/vendedors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(vendedor: IVendedor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendedor);
    return this.http
      .post<IVendedor>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(vendedor: IVendedor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendedor);
    return this.http
      .put<IVendedor>(`${this.resourceUrl}/${getVendedorIdentifier(vendedor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(vendedor: IVendedor): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(vendedor);
    return this.http
      .patch<IVendedor>(`${this.resourceUrl}/${getVendedorIdentifier(vendedor) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IVendedor>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IVendedor[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addVendedorToCollectionIfMissing(vendedorCollection: IVendedor[], ...vendedorsToCheck: (IVendedor | null | undefined)[]): IVendedor[] {
    const vendedors: IVendedor[] = vendedorsToCheck.filter(isPresent);
    if (vendedors.length > 0) {
      const vendedorCollectionIdentifiers = vendedorCollection.map(vendedorItem => getVendedorIdentifier(vendedorItem)!);
      const vendedorsToAdd = vendedors.filter(vendedorItem => {
        const vendedorIdentifier = getVendedorIdentifier(vendedorItem);
        if (vendedorIdentifier == null || vendedorCollectionIdentifiers.includes(vendedorIdentifier)) {
          return false;
        }
        vendedorCollectionIdentifiers.push(vendedorIdentifier);
        return true;
      });
      return [...vendedorsToAdd, ...vendedorCollection];
    }
    return vendedorCollection;
  }

  protected convertDateFromClient(vendedor: IVendedor): IVendedor {
    return Object.assign({}, vendedor, {
      fechaNacimiento: vendedor.fechaNacimiento?.isValid() ? vendedor.fechaNacimiento.format(DATE_FORMAT) : undefined,
      fechaContratacion: vendedor.fechaContratacion?.isValid() ? vendedor.fechaContratacion.format(DATE_FORMAT) : undefined,
      fechaBaja: vendedor.fechaBaja?.isValid() ? vendedor.fechaBaja.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaNacimiento = res.body.fechaNacimiento ? dayjs(res.body.fechaNacimiento) : undefined;
      res.body.fechaContratacion = res.body.fechaContratacion ? dayjs(res.body.fechaContratacion) : undefined;
      res.body.fechaBaja = res.body.fechaBaja ? dayjs(res.body.fechaBaja) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((vendedor: IVendedor) => {
        vendedor.fechaNacimiento = vendedor.fechaNacimiento ? dayjs(vendedor.fechaNacimiento) : undefined;
        vendedor.fechaContratacion = vendedor.fechaContratacion ? dayjs(vendedor.fechaContratacion) : undefined;
        vendedor.fechaBaja = vendedor.fechaBaja ? dayjs(vendedor.fechaBaja) : undefined;
      });
    }
    return res;
  }
}
