import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompradorComponent } from '../list/comprador.component';
import { CompradorDetailComponent } from '../detail/comprador-detail.component';
import { CompradorUpdateComponent } from '../update/comprador-update.component';
import { CompradorRoutingResolveService } from './comprador-routing-resolve.service';
import { Authority } from 'app/config/authority.constants';

const compradorRoute: Routes = [
  {
    path: '',
    component: CompradorComponent,
    data: {
      defaultSort: 'id,asc',
      //Con esta directiva se restringe el acceso escribiendo la ruta a fuego en el buscador
      authority: [Authority.ADMIN, Authority.VENDEDOR],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CompradorDetailComponent,
    resolve: {
      comprador: CompradorRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN, Authority.COMPRADOR],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CompradorUpdateComponent,
    resolve: {
      comprador: CompradorRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompradorUpdateComponent,
    resolve: {
      comprador: CompradorRoutingResolveService,
    },
    data: {
      authority: [Authority.ADMIN],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(compradorRoute)],
  exports: [RouterModule],
})
export class CompradorRoutingModule {}
