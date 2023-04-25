import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { ThemeComponent } from './list/theme.component';
import { ThemeDetailComponent } from './detail/theme-detail.component';
import { ThemeUpdateComponent } from './update/theme-update.component';
import { ThemeDeleteDialogComponent } from './delete/theme-delete-dialog.component';
import { ThemeRoutingModule } from './route/theme-routing.module';

@NgModule({
  imports: [SharedModule, ThemeRoutingModule],
  declarations: [ThemeComponent, ThemeDetailComponent, ThemeUpdateComponent, ThemeDeleteDialogComponent],
})
export class ThemeModule {}
