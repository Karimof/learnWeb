import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { PartComponent } from './list/part.component';
import { PartDetailComponent } from './detail/part-detail.component';
import { PartUpdateComponent } from './update/part-update.component';
import { PartDeleteDialogComponent } from './delete/part-delete-dialog.component';
import { PartRoutingModule } from './route/part-routing.module';

@NgModule({
  imports: [SharedModule, PartRoutingModule],
  declarations: [PartComponent, PartDetailComponent, PartUpdateComponent, PartDeleteDialogComponent],
})
export class PartModule {}
