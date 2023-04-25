import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IMedia, NewMedia } from '../media.model';

export type PartialUpdateMedia = Partial<IMedia> & Pick<IMedia, 'id'>;

export type EntityResponseType = HttpResponse<IMedia>;
export type EntityArrayResponseType = HttpResponse<IMedia[]>;

@Injectable({ providedIn: 'root' })
export class MediaService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/media');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(media: NewMedia): Observable<EntityResponseType> {
    return this.http.post<IMedia>(this.resourceUrl, media, { observe: 'response' });
  }

  update(media: IMedia): Observable<EntityResponseType> {
    return this.http.put<IMedia>(`${this.resourceUrl}/${this.getMediaIdentifier(media)}`, media, { observe: 'response' });
  }

  partialUpdate(media: PartialUpdateMedia): Observable<EntityResponseType> {
    return this.http.patch<IMedia>(`${this.resourceUrl}/${this.getMediaIdentifier(media)}`, media, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IMedia>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IMedia[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getMediaIdentifier(media: Pick<IMedia, 'id'>): number {
    return media.id;
  }

  compareMedia(o1: Pick<IMedia, 'id'> | null, o2: Pick<IMedia, 'id'> | null): boolean {
    return o1 && o2 ? this.getMediaIdentifier(o1) === this.getMediaIdentifier(o2) : o1 === o2;
  }

  addMediaToCollectionIfMissing<Type extends Pick<IMedia, 'id'>>(
    mediaCollection: Type[],
    ...mediaToCheck: (Type | null | undefined)[]
  ): Type[] {
    const media: Type[] = mediaToCheck.filter(isPresent);
    if (media.length > 0) {
      const mediaCollectionIdentifiers = mediaCollection.map(mediaItem => this.getMediaIdentifier(mediaItem)!);
      const mediaToAdd = media.filter(mediaItem => {
        const mediaIdentifier = this.getMediaIdentifier(mediaItem);
        if (mediaCollectionIdentifiers.includes(mediaIdentifier)) {
          return false;
        }
        mediaCollectionIdentifiers.push(mediaIdentifier);
        return true;
      });
      return [...mediaToAdd, ...mediaCollection];
    }
    return mediaCollection;
  }
}
