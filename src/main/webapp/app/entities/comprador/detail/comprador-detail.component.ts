import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IComprador } from '../comprador.model';

@Component({
  selector: 'jhi-comprador-detail',
  templateUrl: './comprador-detail.component.html',
})
export class CompradorDetailComponent implements OnInit {
  comprador: IComprador | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ comprador }) => {
      this.comprador = comprador;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
