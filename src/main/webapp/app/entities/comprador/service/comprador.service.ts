import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IComprador, getCompradorIdentifier } from '../comprador.model';

export type EntityResponseType = HttpResponse<IComprador>;
export type EntityArrayResponseType = HttpResponse<IComprador[]>;

@Injectable({ providedIn: 'root' })
export class CompradorService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/compradors');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(comprador: IComprador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comprador);
    return this.http
      .post<IComprador>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(comprador: IComprador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comprador);
    return this.http
      .put<IComprador>(`${this.resourceUrl}/${getCompradorIdentifier(comprador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(comprador: IComprador): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(comprador);
    return this.http
      .patch<IComprador>(`${this.resourceUrl}/${getCompradorIdentifier(comprador) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IComprador>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IComprador[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addCompradorToCollectionIfMissing(
    compradorCollection: IComprador[],
    ...compradorsToCheck: (IComprador | null | undefined)[]
  ): IComprador[] {
    const compradors: IComprador[] = compradorsToCheck.filter(isPresent);
    if (compradors.length > 0) {
      const compradorCollectionIdentifiers = compradorCollection.map(compradorItem => getCompradorIdentifier(compradorItem)!);
      const compradorsToAdd = compradors.filter(compradorItem => {
        const compradorIdentifier = getCompradorIdentifier(compradorItem);
        if (compradorIdentifier == null || compradorCollectionIdentifiers.includes(compradorIdentifier)) {
          return false;
        }
        compradorCollectionIdentifiers.push(compradorIdentifier);
        return true;
      });
      return [...compradorsToAdd, ...compradorCollection];
    }
    return compradorCollection;
  }

  protected convertDateFromClient(comprador: IComprador): IComprador {
    return Object.assign({}, comprador, {
      fechaNacimiento: comprador.fechaNacimiento?.isValid() ? comprador.fechaNacimiento.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.fechaNacimiento = res.body.fechaNacimiento ? dayjs(res.body.fechaNacimiento) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((comprador: IComprador) => {
        comprador.fechaNacimiento = comprador.fechaNacimiento ? dayjs(comprador.fechaNacimiento) : undefined;
      });
    }
    return res;
  }
}
