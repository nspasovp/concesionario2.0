import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'coche',
        data: { pageTitle: 'concesionario2App.coche.home.title' },
        loadChildren: () => import('./coche/coche.module').then(m => m.CocheModule),
      },
      {
        path: 'venta',
        data: { pageTitle: 'concesionario2App.venta.home.title' },
        loadChildren: () => import('./venta/venta.module').then(m => m.VentaModule),
      },
      {
        path: 'vendedor',
        data: { pageTitle: 'concesionario2App.vendedor.home.title' },
        loadChildren: () => import('./vendedor/vendedor.module').then(m => m.VendedorModule),
      },
      {
        path: 'comprador',
        data: { pageTitle: 'concesionario2App.comprador.home.title' },
        loadChildren: () => import('./comprador/comprador.module').then(m => m.CompradorModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
