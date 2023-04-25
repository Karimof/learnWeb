import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IMedia } from '../media.model';
import { MediaService } from '../service/media.service';

@Injectable({ providedIn: 'root' })
export class MediaRoutingResolveService implements Resolve<IMedia | null> {
  constructor(protected service: MediaService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IMedia | null | never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((media: HttpResponse<IMedia>) => {
          if (media.body) {
            return of(media.body);
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
