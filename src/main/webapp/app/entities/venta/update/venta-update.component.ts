import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IVenta, Venta } from '../venta.model';
import { VentaService } from '../service/venta.service';
import { IVendedor } from 'app/entities/vendedor/vendedor.model';
import { VendedorService } from 'app/entities/vendedor/service/vendedor.service';
import { IComprador } from 'app/entities/comprador/comprador.model';
import { CompradorService } from 'app/entities/comprador/service/comprador.service';
import { CocheService } from 'app/entities/coche/service/coche.service';
import { Coche, ICoche } from 'app/entities/coche/coche.model';

@Component({
  selector: 'jhi-venta-update',
  templateUrl: './venta-update.component.html',
})
export class VentaUpdateComponent implements OnInit {
  isSaving = false;

  vendedorsSharedCollection: IVendedor[] = [];
  compradorsSharedCollection: IComprador[] = [];
  cochesSharedCollection: ICoche[] = [];

  editForm = this.fb.group({
    id: [],
    fecha: [null, [Validators.required]],
    numeroCoches: [null, [Validators.required, Validators.min(1)]],
    vendedor: [null, Validators.required],
    comprador: [null, Validators.required],
    coche: [null, Validators.required],
    numFactura: [null],
  });

  constructor(
    protected ventaService: VentaService,
    protected vendedorService: VendedorService,
    protected cocheService: CocheService,
    protected compradorService: CompradorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ venta }) => {
      this.updateForm(venta);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const venta = this.createFromForm();
    if (venta.id !== undefined) {
      this.subscribeToSaveResponse(this.ventaService.update(venta));
    } else {
      this.subscribeToSaveResponse(this.ventaService.create(venta));
    }
  }

  trackVendedorById(index: number, item: IVendedor): number {
    return item.id!;
  }

  trackCompradorById(index: number, item: IComprador): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IVenta>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(venta: IVenta): void {
    //SETTER
    this.editForm.patchValue({
      id: venta.id,
      fecha: venta.fecha,
      numeroCoches: venta.numeroCoches,
      vendedor: venta.vendedor,
      comprador: venta.comprador,
      numFactura: venta.numFactura,
    });

    this.vendedorsSharedCollection = this.vendedorService.addVendedorToCollectionIfMissing(this.vendedorsSharedCollection, venta.vendedor);
    this.compradorsSharedCollection = this.compradorService.addCompradorToCollectionIfMissing(
      this.compradorsSharedCollection,
      venta.comprador
    );
    this.cochesSharedCollection = this.cocheService.addCocheToCollectionIfMissing(this.cochesSharedCollection);
  }

  protected loadRelationshipsOptions(): void {
    this.vendedorService
      .query()
      .pipe(map((res: HttpResponse<IVendedor[]>) => res.body ?? []))
      .pipe(
        map((vendedors: IVendedor[]) =>
          this.vendedorService.addVendedorToCollectionIfMissing(vendedors, this.editForm.get('vendedor')!.value)
        )
      )
      .subscribe((vendedors: IVendedor[]) => (this.vendedorsSharedCollection = vendedors));

    this.compradorService
      .query()
      .pipe(map((res: HttpResponse<IComprador[]>) => res.body ?? []))
      .pipe(
        map((compradors: IComprador[]) =>
          this.compradorService.addCompradorToCollectionIfMissing(compradors, this.editForm.get('comprador')!.value)
        )
      )
      .subscribe((compradors: IComprador[]) => (this.compradorsSharedCollection = compradors));

    this.cocheService
      .queryD()
      .pipe(map((res: HttpResponse<ICoche[]>) => res.body ?? []))
      .pipe(map((coches: ICoche[]) => this.cocheService.addCocheToCollectionIfMissing(coches, this.editForm.get('coche')!.value)))
      .subscribe((coches: ICoche[]) => (this.cochesSharedCollection = coches));
  }

  protected createFromForm(): IVenta {
    //GETTER
    return {
      ...new Venta(),
      id: this.editForm.get(['id'])!.value,
      fecha: this.editForm.get(['fecha'])!.value,
      numeroCoches: this.editForm.get(['numeroCoches'])!.value,
      vendedor: this.editForm.get(['vendedor'])!.value,
      comprador: this.editForm.get(['comprador'])!.value,
      numFactura: this.editForm.get(['numFactura'])!.value,
    };
  }
}
