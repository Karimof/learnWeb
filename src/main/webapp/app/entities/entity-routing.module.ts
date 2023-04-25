import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'course',
        data: { pageTitle: 'learnWebApp.course.home.title' },
        loadChildren: () => import('./course/course.module').then(m => m.CourseModule),
      },
      {
        path: 'theme',
        data: { pageTitle: 'learnWebApp.theme.home.title' },
        loadChildren: () => import('./theme/theme.module').then(m => m.ThemeModule),
      },
      {
        path: 'part',
        data: { pageTitle: 'learnWebApp.part.home.title' },
        loadChildren: () => import('./part/part.module').then(m => m.PartModule),
      },
      {
        path: 'media',
        data: { pageTitle: 'learnWebApp.media.home.title' },
        loadChildren: () => import('./media/media.module').then(m => m.MediaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
