import { HttpResponse } from '@angular/common/http';
import { Component, OnInit, Optional } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ICoche } from 'app/entities/coche/coche.model';
import { CocheService } from 'app/entities/coche/service/coche.service';
import { map } from 'rxjs/operators';
import { VentaService } from '../service/venta.service';

import { IVenta } from '../venta.model';

@Component({
  selector: 'jhi-venta-detail',
  templateUrl: './venta-detail.component.html',
})
export class VentaDetailComponent implements OnInit {
  venta: IVenta | null = null;
  coches: number | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected ventaService: VentaService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      this.loadRelationshipsOptions();

      //poner aqui el numero de coches vendidos
      // this.venta?.numeroCoches=this.ventaService.findNumCoches;
    });
  }

  previousState(): void {
    window.history.back();
  }

  protected loadRelationshipsOptions(): void {
    if (this.venta?.id !== undefined) {
      this.ventaService.findNumCoches(this.venta.id).subscribe(coche => (this.coches = coche.body));
    }
  }
}
