import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { DATE_FORMAT } from 'app/config/input.constants';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IIntencao, getIntencaoIdentifier } from '../intencao.model';

export type EntityResponseType = HttpResponse<IIntencao>;
export type EntityArrayResponseType = HttpResponse<IIntencao[]>;

@Injectable({ providedIn: 'root' })
export class IntencaoService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/intencaos');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(intencao: IIntencao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intencao);
    return this.http
      .post<IIntencao>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(intencao: IIntencao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intencao);
    return this.http
      .put<IIntencao>(`${this.resourceUrl}/${getIntencaoIdentifier(intencao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(intencao: IIntencao): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(intencao);
    return this.http
      .patch<IIntencao>(`${this.resourceUrl}/${getIntencaoIdentifier(intencao) as number}`, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IIntencao>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IIntencao[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addIntencaoToCollectionIfMissing(intencaoCollection: IIntencao[], ...intencaosToCheck: (IIntencao | null | undefined)[]): IIntencao[] {
    const intencaos: IIntencao[] = intencaosToCheck.filter(isPresent);
    if (intencaos.length > 0) {
      const intencaoCollectionIdentifiers = intencaoCollection.map(intencaoItem => getIntencaoIdentifier(intencaoItem)!);
      const intencaosToAdd = intencaos.filter(intencaoItem => {
        const intencaoIdentifier = getIntencaoIdentifier(intencaoItem);
        if (intencaoIdentifier == null || intencaoCollectionIdentifiers.includes(intencaoIdentifier)) {
          return false;
        }
        intencaoCollectionIdentifiers.push(intencaoIdentifier);
        return true;
      });
      return [...intencaosToAdd, ...intencaoCollection];
    }
    return intencaoCollection;
  }

  protected convertDateFromClient(intencao: IIntencao): IIntencao {
    return Object.assign({}, intencao, {
      data: intencao.data?.isValid() ? intencao.data.format(DATE_FORMAT) : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.data = res.body.data ? dayjs(res.body.data) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((intencao: IIntencao) => {
        intencao.data = intencao.data ? dayjs(intencao.data) : undefined;
      });
    }
    return res;
  }
}
