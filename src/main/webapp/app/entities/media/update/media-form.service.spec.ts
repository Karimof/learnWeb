import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../media.test-samples';

import { MediaFormService } from './media-form.service';

describe('Media Form Service', () => {
  let service: MediaFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MediaFormService);
  });

  describe('Service methods', () => {
    describe('createMediaFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMediaFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            photo: expect.any(Object),
            video: expect.any(Object),
            theme: expect.any(Object),
          })
        );
      });

      it('passing IMedia should create a new form with FormGroup', () => {
        const formGroup = service.createMediaFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            title: expect.any(Object),
            description: expect.any(Object),
            photo: expect.any(Object),
            video: expect.any(Object),
            theme: expect.any(Object),
          })
        );
      });
    });

    describe('getMedia', () => {
      it('should return NewMedia for default Media initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMediaFormGroup(sampleWithNewData);

        const media = service.getMedia(formGroup) as any;

        expect(media).toMatchObject(sampleWithNewData);
      });

      it('should return NewMedia for empty Media initial value', () => {
        const formGroup = service.createMediaFormGroup();

        const media = service.getMedia(formGroup) as any;

        expect(media).toMatchObject({});
      });

      it('should return IMedia', () => {
        const formGroup = service.createMediaFormGroup(sampleWithRequiredData);

        const media = service.getMedia(formGroup) as any;

        expect(media).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMedia should not enable id FormControl', () => {
        const formGroup = service.createMediaFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMedia should disable id FormControl', () => {
        const formGroup = service.createMediaFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
