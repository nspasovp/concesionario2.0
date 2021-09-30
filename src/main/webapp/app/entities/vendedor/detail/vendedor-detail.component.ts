import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { VentaService } from 'app/entities/venta/service/venta.service';

import { IVendedor } from '../vendedor.model';

@Component({
  selector: 'jhi-vendedor-detail',
  templateUrl: './vendedor-detail.component.html',
})
export class VendedorDetailComponent implements OnInit {
  vendedor: IVendedor | null = null;

  constructor(protected activatedRoute: ActivatedRoute, protected ventaService: VentaService) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ vendedor }) => {
      this.vendedor = vendedor;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
