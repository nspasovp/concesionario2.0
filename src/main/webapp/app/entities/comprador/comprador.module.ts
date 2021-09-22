import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CompradorComponent } from './list/comprador.component';
import { CompradorDetailComponent } from './detail/comprador-detail.component';
import { CompradorUpdateComponent } from './update/comprador-update.component';
import { CompradorDeleteDialogComponent } from './delete/comprador-delete-dialog.component';
import { CompradorRoutingModule } from './route/comprador-routing.module';

@NgModule({
  imports: [SharedModule, CompradorRoutingModule],
  declarations: [CompradorComponent, CompradorDetailComponent, CompradorUpdateComponent, CompradorDeleteDialogComponent],
  entryComponents: [CompradorDeleteDialogComponent],
})
export class CompradorModule {}
