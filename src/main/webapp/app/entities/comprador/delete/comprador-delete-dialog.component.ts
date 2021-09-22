import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IComprador } from '../comprador.model';
import { CompradorService } from '../service/comprador.service';

@Component({
  templateUrl: './comprador-delete-dialog.component.html',
})
export class CompradorDeleteDialogComponent {
  comprador?: IComprador;

  constructor(protected compradorService: CompradorService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.compradorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
