import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { MediaFormService, MediaFormGroup } from './media-form.service';
import { IMedia } from '../media.model';
import { MediaService } from '../service/media.service';
import { ITheme } from 'app/entities/theme/theme.model';
import { ThemeService } from 'app/entities/theme/service/theme.service';

@Component({
  selector: 'jhi-media-update',
  templateUrl: './media-update.component.html',
})
export class MediaUpdateComponent implements OnInit {
  isSaving = false;
  media: IMedia | null = null;

  themesSharedCollection: ITheme[] = [];

  editForm: MediaFormGroup = this.mediaFormService.createMediaFormGroup();

  constructor(
    protected mediaService: MediaService,
    protected mediaFormService: MediaFormService,
    protected themeService: ThemeService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTheme = (o1: ITheme | null, o2: ITheme | null): boolean => this.themeService.compareTheme(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ media }) => {
      this.media = media;
      if (media) {
        this.updateForm(media);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const media = this.mediaFormService.getMedia(this.editForm);
    if (media.id !== null) {
      this.subscribeToSaveResponse(this.mediaService.update(media));
    } else {
      this.subscribeToSaveResponse(this.mediaService.create(media));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IMedia>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
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

  protected updateForm(media: IMedia): void {
    this.media = media;
    this.mediaFormService.resetForm(this.editForm, media);

    this.themesSharedCollection = this.themeService.addThemeToCollectionIfMissing<ITheme>(this.themesSharedCollection, media.theme);
  }

  protected loadRelationshipsOptions(): void {
    this.themeService
      .query()
      .pipe(map((res: HttpResponse<ITheme[]>) => res.body ?? []))
      .pipe(map((themes: ITheme[]) => this.themeService.addThemeToCollectionIfMissing<ITheme>(themes, this.media?.theme)))
      .subscribe((themes: ITheme[]) => (this.themesSharedCollection = themes));
  }
}
