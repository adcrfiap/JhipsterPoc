import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { IntencaoComponent } from '../list/intencao.component';
import { IntencaoDetailComponent } from '../detail/intencao-detail.component';
import { IntencaoUpdateComponent } from '../update/intencao-update.component';
import { IntencaoRoutingResolveService } from './intencao-routing-resolve.service';

const intencaoRoute: Routes = [
  {
    path: '',
    component: IntencaoComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: IntencaoDetailComponent,
    resolve: {
      intencao: IntencaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: IntencaoUpdateComponent,
    resolve: {
      intencao: IntencaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: IntencaoUpdateComponent,
    resolve: {
      intencao: IntencaoRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(intencaoRoute)],
  exports: [RouterModule],
})
export class IntencaoRoutingModule {}
