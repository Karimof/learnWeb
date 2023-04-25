import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { MediaComponent } from './list/media.component';
import { MediaDetailComponent } from './detail/media-detail.component';
import { MediaUpdateComponent } from './update/media-update.component';
import { MediaDeleteDialogComponent } from './delete/media-delete-dialog.component';
import { MediaRoutingModule } from './route/media-routing.module';

@NgModule({
  imports: [SharedModule, MediaRoutingModule],
  declarations: [MediaComponent, MediaDetailComponent, MediaUpdateComponent, MediaDeleteDialogComponent],
})
export class MediaModule {}
