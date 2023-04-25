import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../theme.test-samples';

import { ThemeFormService } from './theme-form.service';

describe('Theme Form Service', () => {
  let service: ThemeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ThemeFormService);
  });

  describe('Service methods', () => {
    describe('createThemeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createThemeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            course: expect.any(Object),
          })
        );
      });

      it('passing ITheme should create a new form with FormGroup', () => {
        const formGroup = service.createThemeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            course: expect.any(Object),
          })
        );
      });
    });

    describe('getTheme', () => {
      it('should return NewTheme for default Theme initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createThemeFormGroup(sampleWithNewData);

        const theme = service.getTheme(formGroup) as any;

        expect(theme).toMatchObject(sampleWithNewData);
      });

      it('should return NewTheme for empty Theme initial value', () => {
        const formGroup = service.createThemeFormGroup();

        const theme = service.getTheme(formGroup) as any;

        expect(theme).toMatchObject({});
      });

      it('should return ITheme', () => {
        const formGroup = service.createThemeFormGroup(sampleWithRequiredData);

        const theme = service.getTheme(formGroup) as any;

        expect(theme).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITheme should not enable id FormControl', () => {
        const formGroup = service.createThemeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTheme should disable id FormControl', () => {
        const formGroup = service.createThemeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
