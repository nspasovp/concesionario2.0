import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompradorComponent } from '../list/comprador.component';
import { CompradorDetailComponent } from '../detail/comprador-detail.component';
import { CompradorUpdateComponent } from '../update/comprador-update.component';
import { CompradorRoutingResolveService } from './comprador-routing-resolve.service';

const compradorRoute: Routes = [
  {
    path: '',
    component: CompradorComponent,
    data: {
      defaultSort: 'id,asc',
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompradorDetailComponent,
    resolve: {
      comprador: CompradorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompradorUpdateComponent,
    resolve: {
      comprador: CompradorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompradorUpdateComponent,
    resolve: {
      comprador: CompradorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compradorRoute)],
  exports: [RouterModule],
})
export class CompradorRoutingModule {}
