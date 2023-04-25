import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { MediaComponent } from '../list/media.component';
import { MediaDetailComponent } from '../detail/media-detail.component';
import { MediaUpdateComponent } from '../update/media-update.component';
import { MediaRoutingResolveService } from './media-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const mediaRoute: Routes = [
  {
    path: '',
    component: MediaComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: MediaDetailComponent,
    resolve: {
      media: MediaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: MediaUpdateComponent,
    resolve: {
      media: MediaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: MediaUpdateComponent,
    resolve: {
      media: MediaRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(mediaRoute)],
  exports: [RouterModule],
})
export class MediaRoutingModule {}
