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

  constructor(protected activatedRoute: ActivatedRoute, protected ventaService: VentaService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.venta = venta;
      //poner aqui el numero de coches vendidos
      venta.set(this.ventaService.findNumCoches(this.venta?.id!));
      // this.venta?.numeroCoches=this.ventaService.findNumCoches;
    });
  }

  /*protected loadRelationshipsOptions(): void {
    this.ventaService
      .findNumCoches(this.venta?.id!)
      .pipe(map((res: HttpResponse<ICoche[]>) => res.body ?? []))
      .subscribe((coche: ICoche[]) => (this.coches = coche));

      }*/

  previousState(): void {
    window.history.back();
  }
}
