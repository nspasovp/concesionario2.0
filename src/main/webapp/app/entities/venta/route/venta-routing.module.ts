import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { VentaComponent } from '../list/venta.component';
import { VentaDetailComponent } from '../detail/venta-detail.component';
import { VentaUpdateComponent } from '../update/venta-update.component';
import { VentaRoutingResolveService } from './venta-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const ventaRoute: Routes = [
  {
    path: '',
    component: VentaComponent,
    data: {
      defaultSort: 'id,asc',
      authority: [Authority.ADMIN, Authority.VENDEDOR],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: VentaDetailComponent,
    resolve: {
      venta: VentaRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: VentaUpdateComponent,
    resolve: {
      venta: VentaRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN, Authority.VENDEDOR],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: VentaUpdateComponent,
    resolve: {
      venta: VentaRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN, Authority.VENDEDOR],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(ventaRoute)],
  exports: [RouterModule],
})
export class VentaRoutingModule {}
