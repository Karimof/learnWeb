import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { MediaFormService } from './media-form.service';
import { MediaService } from '../service/media.service';
import { IMedia } from '../media.model';
import { ITheme } from 'app/entities/theme/theme.model';
import { ThemeService } from 'app/entities/theme/service/theme.service';

import { MediaUpdateComponent } from './media-update.component';

describe('Media Management Update Component', () => {
  let comp: MediaUpdateComponent;
  let fixture: ComponentFixture<MediaUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let mediaFormService: MediaFormService;
  let mediaService: MediaService;
  let themeService: ThemeService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [MediaUpdateComponent],
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
      .overrideTemplate(MediaUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(MediaUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    mediaFormService = TestBed.inject(MediaFormService);
    mediaService = TestBed.inject(MediaService);
    themeService = TestBed.inject(ThemeService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Theme query and add missing value', () => {
      const media: IMedia = { id: 456 };
      const theme: ITheme = { id: 71556 };
      media.theme = theme;

      const themeCollection: ITheme[] = [{ id: 4215 }];
      jest.spyOn(themeService, 'query').mockReturnValue(of(new HttpResponse({ body: themeCollection })));
      const additionalThemes = [theme];
      const expectedCollection: ITheme[] = [...additionalThemes, ...themeCollection];
      jest.spyOn(themeService, 'addThemeToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ media });
      comp.ngOnInit();

      expect(themeService.query).toHaveBeenCalled();
      expect(themeService.addThemeToCollectionIfMissing).toHaveBeenCalledWith(
        themeCollection,
        ...additionalThemes.map(expect.objectContaining)
      );
      expect(comp.themesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const media: IMedia = { id: 456 };
      const theme: ITheme = { id: 92121 };
      media.theme = theme;

      activatedRoute.data = of({ media });
      comp.ngOnInit();

      expect(comp.themesSharedCollection).toContain(theme);
      expect(comp.media).toEqual(media);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedia>>();
      const media = { id: 123 };
      jest.spyOn(mediaFormService, 'getMedia').mockReturnValue(media);
      jest.spyOn(mediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ media });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: media }));
      saveSubject.complete();

      // THEN
      expect(mediaFormService.getMedia).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(mediaService.update).toHaveBeenCalledWith(expect.objectContaining(media));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedia>>();
      const media = { id: 123 };
      jest.spyOn(mediaFormService, 'getMedia').mockReturnValue({ id: null });
      jest.spyOn(mediaService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ media: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: media }));
      saveSubject.complete();

      // THEN
      expect(mediaFormService.getMedia).toHaveBeenCalled();
      expect(mediaService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<IMedia>>();
      const media = { id: 123 };
      jest.spyOn(mediaService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ media });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(mediaService.update).toHaveBeenCalled();
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
  });
});
