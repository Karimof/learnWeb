import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IPart, NewPart } from '../part.model';

export type PartialUpdatePart = Partial<IPart> & Pick<IPart, 'id'>;

export type EntityResponseType = HttpResponse<IPart>;
export type EntityArrayResponseType = HttpResponse<IPart[]>;

@Injectable({ providedIn: 'root' })
export class PartService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/parts');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(part: NewPart): Observable<EntityResponseType> {
    return this.http.post<IPart>(this.resourceUrl, part, { observe: 'response' });
  }

  update(part: IPart): Observable<EntityResponseType> {
    return this.http.put<IPart>(`${this.resourceUrl}/${this.getPartIdentifier(part)}`, part, { observe: 'response' });
  }

  partialUpdate(part: PartialUpdatePart): Observable<EntityResponseType> {
    return this.http.patch<IPart>(`${this.resourceUrl}/${this.getPartIdentifier(part)}`, part, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IPart>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IPart[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getPartIdentifier(part: Pick<IPart, 'id'>): number {
    return part.id;
  }

  comparePart(o1: Pick<IPart, 'id'> | null, o2: Pick<IPart, 'id'> | null): boolean {
    return o1 && o2 ? this.getPartIdentifier(o1) === this.getPartIdentifier(o2) : o1 === o2;
  }

  addPartToCollectionIfMissing<Type extends Pick<IPart, 'id'>>(
    partCollection: Type[],
    ...partsToCheck: (Type | null | undefined)[]
  ): Type[] {
    const parts: Type[] = partsToCheck.filter(isPresent);
    if (parts.length > 0) {
      const partCollectionIdentifiers = partCollection.map(partItem => this.getPartIdentifier(partItem)!);
      const partsToAdd = parts.filter(partItem => {
        const partIdentifier = this.getPartIdentifier(partItem);
        if (partCollectionIdentifiers.includes(partIdentifier)) {
          return false;
        }
        partCollectionIdentifiers.push(partIdentifier);
        return true;
      });
      return [...partsToAdd, ...partCollection];
    }
    return partCollection;
  }
}
