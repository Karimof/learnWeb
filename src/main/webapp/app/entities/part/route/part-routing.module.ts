import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { PartComponent } from '../list/part.component';
import { PartDetailComponent } from '../detail/part-detail.component';
import { PartUpdateComponent } from '../update/part-update.component';
import { PartRoutingResolveService } from './part-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const partRoute: Routes = [
  {
    path: '',
    component: PartComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: PartDetailComponent,
    resolve: {
      part: PartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: PartUpdateComponent,
    resolve: {
      part: PartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: PartUpdateComponent,
    resolve: {
      part: PartRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(partRoute)],
  exports: [RouterModule],
})
export class PartRoutingModule {}
