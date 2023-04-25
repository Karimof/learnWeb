import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ThemeComponent } from '../list/theme.component';
import { ThemeDetailComponent } from '../detail/theme-detail.component';
import { ThemeUpdateComponent } from '../update/theme-update.component';
import { ThemeRoutingResolveService } from './theme-routing-resolve.service';
import { ASC } from 'app/config/navigation.constants';

const themeRoute: Routes = [
  {
    path: '',
    component: ThemeComponent,
    data: {
      defaultSort: 'id,' + ASC,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ThemeDetailComponent,
    resolve: {
      theme: ThemeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ThemeUpdateComponent,
    resolve: {
      theme: ThemeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ThemeUpdateComponent,
    resolve: {
      theme: ThemeRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(themeRoute)],
  exports: [RouterModule],
})
export class ThemeRoutingModule {}
