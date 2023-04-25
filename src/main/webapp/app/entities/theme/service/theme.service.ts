import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ITheme, NewTheme } from '../theme.model';

export type PartialUpdateTheme = Partial<ITheme> & Pick<ITheme, 'id'>;

export type EntityResponseType = HttpResponse<ITheme>;
export type EntityArrayResponseType = HttpResponse<ITheme[]>;

@Injectable({ providedIn: 'root' })
export class ThemeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/themes');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(theme: NewTheme): Observable<EntityResponseType> {
    return this.http.post<ITheme>(this.resourceUrl, theme, { observe: 'response' });
  }

  update(theme: ITheme): Observable<EntityResponseType> {
    return this.http.put<ITheme>(`${this.resourceUrl}/${this.getThemeIdentifier(theme)}`, theme, { observe: 'response' });
  }

  partialUpdate(theme: PartialUpdateTheme): Observable<EntityResponseType> {
    return this.http.patch<ITheme>(`${this.resourceUrl}/${this.getThemeIdentifier(theme)}`, theme, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ITheme>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ITheme[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  getThemeIdentifier(theme: Pick<ITheme, 'id'>): number {
    return theme.id;
  }

  compareTheme(o1: Pick<ITheme, 'id'> | null, o2: Pick<ITheme, 'id'> | null): boolean {
    return o1 && o2 ? this.getThemeIdentifier(o1) === this.getThemeIdentifier(o2) : o1 === o2;
  }

  addThemeToCollectionIfMissing<Type extends Pick<ITheme, 'id'>>(
    themeCollection: Type[],
    ...themesToCheck: (Type | null | undefined)[]
  ): Type[] {
    const themes: Type[] = themesToCheck.filter(isPresent);
    if (themes.length > 0) {
      const themeCollectionIdentifiers = themeCollection.map(themeItem => this.getThemeIdentifier(themeItem)!);
      const themesToAdd = themes.filter(themeItem => {
        const themeIdentifier = this.getThemeIdentifier(themeItem);
        if (themeCollectionIdentifiers.includes(themeIdentifier)) {
          return false;
        }
        themeCollectionIdentifiers.push(themeIdentifier);
        return true;
      });
      return [...themesToAdd, ...themeCollection];
    }
    return themeCollection;
  }
}
