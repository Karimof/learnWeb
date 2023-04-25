import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ITheme } from '../theme.model';
import { ThemeService } from '../service/theme.service';

@Injectable({ providedIn: 'root' })
export class ThemeRoutingResolveService implements Resolve<ITheme | null> {
  constructor(protected service: ThemeService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ITheme | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((theme: HttpResponse<ITheme>) => {
          if (theme.body) {
            return of(theme.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(null);
  }
}
