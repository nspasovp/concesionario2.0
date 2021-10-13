import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VendedorComponent } from '../list/vendedor.component';
import { VendedorDetailComponent } from '../detail/vendedor-detail.component';
import { VendedorUpdateComponent } from '../update/vendedor-update.component';
import { VendedorRoutingResolveService } from './vendedor-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const vendedorRoute: Routes = [
  {
    path: '',
    component: VendedorComponent,
    data: {
      defaultSort: 'id,asc',
      authority: [Authority.ADMIN, Authority.VENDEDOR],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VendedorDetailComponent,
    resolve: {
      vendedor: VendedorRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN, Authority.VENDEDOR],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VendedorUpdateComponent,
    resolve: {
      vendedor: VendedorRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VendedorUpdateComponent,
    resolve: {
      vendedor: VendedorRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(vendedorRoute)],
  exports: [RouterModule],
})
export class VendedorRoutingModule {}
