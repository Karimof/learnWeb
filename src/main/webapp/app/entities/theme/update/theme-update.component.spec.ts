import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, Subject, from } from 'rxjs';

import { ThemeFormService } from './theme-form.service';
import { ThemeService } from '../service/theme.service';
import { ITheme } from '../theme.model';
import { ICourse } from 'app/entities/course/course.model';
import { CourseService } from 'app/entities/course/service/course.service';

import { ThemeUpdateComponent } from './theme-update.component';

describe('Theme Management Update Component', () => {
  let comp: ThemeUpdateComponent;
  let fixture: ComponentFixture<ThemeUpdateComponent>;
  let activatedRoute: ActivatedRoute;
  let themeFormService: ThemeFormService;
  let themeService: ThemeService;
  let courseService: CourseService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule, RouterTestingModule.withRoutes([])],
      declarations: [ThemeUpdateComponent],
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
      .overrideTemplate(ThemeUpdateComponent, '')
      .compileComponents();

    fixture = TestBed.createComponent(ThemeUpdateComponent);
    activatedRoute = TestBed.inject(ActivatedRoute);
    themeFormService = TestBed.inject(ThemeFormService);
    themeService = TestBed.inject(ThemeService);
    courseService = TestBed.inject(CourseService);

    comp = fixture.componentInstance;
  });

  describe('ngOnInit', () => {
    it('Should call Course query and add missing value', () => {
      const theme: ITheme = { id: 456 };
      const course: ICourse = { id: 23735 };
      theme.course = course;

      const courseCollection: ICourse[] = [{ id: 57450 }];
      jest.spyOn(courseService, 'query').mockReturnValue(of(new HttpResponse({ body: courseCollection })));
      const additionalCourses = [course];
      const expectedCollection: ICourse[] = [...additionalCourses, ...courseCollection];
      jest.spyOn(courseService, 'addCourseToCollectionIfMissing').mockReturnValue(expectedCollection);

      activatedRoute.data = of({ theme });
      comp.ngOnInit();

      expect(courseService.query).toHaveBeenCalled();
      expect(courseService.addCourseToCollectionIfMissing).toHaveBeenCalledWith(
        courseCollection,
        ...additionalCourses.map(expect.objectContaining)
      );
      expect(comp.coursesSharedCollection).toEqual(expectedCollection);
    });

    it('Should update editForm', () => {
      const theme: ITheme = { id: 456 };
      const course: ICourse = { id: 97407 };
      theme.course = course;

      activatedRoute.data = of({ theme });
      comp.ngOnInit();

      expect(comp.coursesSharedCollection).toContain(course);
      expect(comp.theme).toEqual(theme);
    });
  });

  describe('save', () => {
    it('Should call update service on save for existing entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITheme>>();
      const theme = { id: 123 };
      jest.spyOn(themeFormService, 'getTheme').mockReturnValue(theme);
      jest.spyOn(themeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ theme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: theme }));
      saveSubject.complete();

      // THEN
      expect(themeFormService.getTheme).toHaveBeenCalled();
      expect(comp.previousState).toHaveBeenCalled();
      expect(themeService.update).toHaveBeenCalledWith(expect.objectContaining(theme));
      expect(comp.isSaving).toEqual(false);
    });

    it('Should call create service on save for new entity', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITheme>>();
      const theme = { id: 123 };
      jest.spyOn(themeFormService, 'getTheme').mockReturnValue({ id: null });
      jest.spyOn(themeService, 'create').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ theme: null });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.next(new HttpResponse({ body: theme }));
      saveSubject.complete();

      // THEN
      expect(themeFormService.getTheme).toHaveBeenCalled();
      expect(themeService.create).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).toHaveBeenCalled();
    });

    it('Should set isSaving to false on error', () => {
      // GIVEN
      const saveSubject = new Subject<HttpResponse<ITheme>>();
      const theme = { id: 123 };
      jest.spyOn(themeService, 'update').mockReturnValue(saveSubject);
      jest.spyOn(comp, 'previousState');
      activatedRoute.data = of({ theme });
      comp.ngOnInit();

      // WHEN
      comp.save();
      expect(comp.isSaving).toEqual(true);
      saveSubject.error('This is an error!');

      // THEN
      expect(themeService.update).toHaveBeenCalled();
      expect(comp.isSaving).toEqual(false);
      expect(comp.previousState).not.toHaveBeenCalled();
    });
  });

  describe('Compare relationships', () => {
    describe('compareCourse', () => {
      it('Should forward to courseService', () => {
        const entity = { id: 123 };
        const entity2 = { id: 456 };
        jest.spyOn(courseService, 'compareCourse');
        comp.compareCourse(entity, entity2);
        expect(courseService.compareCourse).toHaveBeenCalledWith(entity, entity2);
      });
    });
  });
});
