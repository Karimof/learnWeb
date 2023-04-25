import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { PartFormService } from './part-form.service';
import { PartService } from '../service/part.service';
import { IPart } from '../part.model';
import { ITheme } from 'app/entities/theme/theme.model';
import { ThemeService } from 'app/entities/theme/service/theme.service';
import { IMedia } from 'app/entities/media/media.model';
import { MediaService } from 'app/entities/media/service/media.service';

import { PartUpdateComponent } from './part-update.component';

describe('Part Management Update Component', () => {
  let comp: PartUpdateComponent;
  let fixture: ComponentFixture<PartUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let partFormService: PartFormService;
  let partService: PartService;
  let themeService: ThemeService;
  let mediaService: MediaService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [PartUpdateComponent],
      providers: [
        FormBuilder,
        {
          provide: ActivatedRoute,
          useValue: {
            params: from([{}]),
          },
        },
      ],
    })
      .overrideTemplate(PartUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(PartUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    partFormService = TestBed.inject(PartFormService);
    partService = TestBed.inject(PartService);
    themeService = TestBed.inject(ThemeService);
    mediaService = TestBed.inject(MediaService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Theme query and add missing value', () => {
      const part: IPart = { id: 456 };
      const theme: ITheme = { id: 51001 };
      part.theme = theme;

      const themeCollection: ITheme[] = [{ id: 96556 }];
      jest.spyOn(themeService, 'query').mockReturnValue(of(new HttpResponse({ body: themeCollection })));
      const additionalThemes = [theme];
      const expectedCollection: ITheme[] = [...additionalThemes, ...themeCollection];
      jest.spyOn(themeService, 'addThemeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(themeService.query).toHaveBeenCalled();
      expect(themeService.addThemeToCollectionIfMissing).toHaveBeenCalledWith(
        themeCollection,
        ...additionalThemes.map(expect.objectContaining)
      );
      expect(comp.themesSharedCollection).toEqual(expectedCollection);
    });

    it('Should call Media query and add missing value', () => {
      const part: IPart = { id: 456 };
      const media: IMedia = { id: 7091 };
      part.media = media;

      const mediaCollection: IMedia[] = [{ id: 61813 }];
      jest.spyOn(mediaService, 'query').mockReturnValue(of(new HttpResponse({ body: mediaCollection })));
      const additionalMedia = [media];
      const expectedCollection: IMedia[] = [...additionalMedia, ...mediaCollection];
      jest.spyOn(mediaService, 'addMediaToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(mediaService.query).toHaveBeenCalled();
      expect(mediaService.addMediaToCollectionIfMissing).toHaveBeenCalledWith(
        mediaCollection,
        ...additionalMedia.map(expect.objectContaining)
      );
      expect(comp.mediaSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const part: IPart = { id: 456 };
      const theme: ITheme = { id: 48890 };
      part.theme = theme;
      const media: IMedia = { id: 7888 };
      part.media = media;

      activatedRoute.data = of({ part });
      comp.ngOnInit();

      expect(comp.themesSharedCollection).toContain(theme);
      expect(comp.mediaSharedCollection).toContain(media);
      expect(comp.part).toEqual(part);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPart>>();
      const part = { id: 123 };
      jest.spyOn(partFormService, 'getPart').mockReturnValue(part);
      jest.spyOn(partService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: part }));
      saveSubject.complete();

      // THEN
      expect(partFormService.getPart).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(partService.update).toHaveBeenCalledWith(expect.objectContaining(part));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPart>>();
      const part = { id: 123 };
      jest.spyOn(partFormService, 'getPart').mockReturnValue({ id: null });
      jest.spyOn(partService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: part }));
      saveSubject.complete();

      // THEN
      expect(partFormService.getPart).toHaveBeenCalled();
      expect(partService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IPart>>();
      const part = { id: 123 };
      jest.spyOn(partService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ part });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(partService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareTheme', () => {
      it('Should forward to themeService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(themeService, 'compareTheme');
        comp.compareTheme(entity, entity2);
        expect(themeService.compareTheme).toHaveBeenCalledWith(entity, entity2);
      });
    });

    describe('compareMedia', () => {
      it('Should forward to mediaService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(mediaService, 'compareMedia');
        comp.compareMedia(entity, entity2);
        expect(mediaService.compareMedia).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
