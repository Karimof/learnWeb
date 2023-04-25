import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { PartFormService, PartFormGroup } from './part-form.service';
import { IPart } from '../part.model';
import { PartService } from '../service/part.service';
import { ITheme } from 'app/entities/theme/theme.model';
import { ThemeService } from 'app/entities/theme/service/theme.service';
import { IMedia } from 'app/entities/media/media.model';
import { MediaService } from 'app/entities/media/service/media.service';

@Component({
  selector: 'jhi-part-update',
  templateUrl: './part-update.component.html',
})
export class PartUpdateComponent implements OnInit {
  isSaving = false;
  part: IPart | null = null;

  themesSharedCollection: ITheme[] = [];
  mediaSharedCollection: IMedia[] = [];

  editForm: PartFormGroup = this.partFormService.createPartFormGroup();

  constructor(
    protected partService: PartService,
    protected partFormService: PartFormService,
    protected themeService: ThemeService,
    protected mediaService: MediaService,
    protected activatedRoute: ActivatedRoute
  ) {}

  compareTheme = (o1: ITheme | null, o2: ITheme | null): boolean => this.themeService.compareTheme(o1, o2);

  compareMedia = (o1: IMedia | null, o2: IMedia | null): boolean => this.mediaService.compareMedia(o1, o2);

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ part }) => {
      this.part = part;
      if (part) {
        this.updateForm(part);
      }

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const part = this.partFormService.getPart(this.editForm);
    if (part.id !== null) {
      this.subscribeToSaveResponse(this.partService.update(part));
    } else {
      this.subscribeToSaveResponse(this.partService.create(part));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPart>>): void {
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

  protected updateForm(part: IPart): void {
    this.part = part;
    this.partFormService.resetForm(this.editForm, part);

    this.themesSharedCollection = this.themeService.addThemeToCollectionIfMissing<ITheme>(this.themesSharedCollection, part.theme);
    this.mediaSharedCollection = this.mediaService.addMediaToCollectionIfMissing<IMedia>(this.mediaSharedCollection, part.media);
  }

  protected loadRelationshipsOptions(): void {
    this.themeService
      .query()
      .pipe(map((res: HttpResponse<ITheme[]>) => res.body ?? []))
      .pipe(map((themes: ITheme[]) => this.themeService.addThemeToCollectionIfMissing<ITheme>(themes, this.part?.theme)))
      .subscribe((themes: ITheme[]) => (this.themesSharedCollection = themes));

    this.mediaService
      .query()
      .pipe(map((res: HttpResponse<IMedia[]>) => res.body ?? []))
      .pipe(map((media: IMedia[]) => this.mediaService.addMediaToCollectionIfMissing<IMedia>(media, this.part?.media)))
      .subscribe((media: IMedia[]) => (this.mediaSharedCollection = media));
  }
}
