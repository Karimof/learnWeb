import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { IPart, NewPart } from '../part.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts IPart for edit and NewPartFormGroupInput for create.
 */
type PartFormGroupInput = IPart | PartialWithRequiredKeyOf<NewPart>;

type PartFormDefaults = Pick<NewPart, 'id'>;

type PartFormGroupContent = {
  id: FormControl<IPart['id'] | NewPart['id']>;
  title: FormControl<IPart['title']>;
  description: FormControl<IPart['description']>;
  question: FormControl<IPart['question']>;
  codeTitle: FormControl<IPart['codeTitle']>;
  codeDescription: FormControl<IPart['codeDescription']>;
  code: FormControl<IPart['code']>;
  fullCode: FormControl<IPart['fullCode']>;
  result: FormControl<IPart['result']>;
  additional: FormControl<IPart['additional']>;
  theme: FormControl<IPart['theme']>;
  media: FormControl<IPart['media']>;
};

export type PartFormGroup = FormGroup<PartFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class PartFormService {
  createPartFormGroup(part: PartFormGroupInput = { id: null }): PartFormGroup {
    const partRawValue = {
      ...this.getFormDefaults(),
      ...part,
    };
    return new FormGroup<PartFormGroupContent>({
      id: new FormControl(
        { value: partRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(partRawValue.title, {
        validators: [Validators.required],
      }),
      description: new FormControl(partRawValue.description, {
        validators: [Validators.maxLength(5000)],
      }),
      question: new FormControl(partRawValue.question, {
        validators: [Validators.maxLength(1000)],
      }),
      codeTitle: new FormControl(partRawValue.codeTitle, {
        validators: [Validators.maxLength(1000)],
      }),
      codeDescription: new FormControl(partRawValue.codeDescription, {
        validators: [Validators.maxLength(3000)],
      }),
      code: new FormControl(partRawValue.code, {
        validators: [Validators.maxLength(3000)],
      }),
      fullCode: new FormControl(partRawValue.fullCode, {
        validators: [Validators.maxLength(5000)],
      }),
      result: new FormControl(partRawValue.result, {
        validators: [Validators.maxLength(1000)],
      }),
      additional: new FormControl(partRawValue.additional, {
        validators: [Validators.maxLength(3000)],
      }),
      theme: new FormControl(partRawValue.theme),
      media: new FormControl(partRawValue.media),
    });
  }

  getPart(form: PartFormGroup): IPart | NewPart {
    return form.getRawValue() as IPart | NewPart;
  }

  resetForm(form: PartFormGroup, part: PartFormGroupInput): void {
    const partRawValue = { ...this.getFormDefaults(), ...part };
    form.reset(
      {
        ...partRawValue,
        id: { value: partRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): PartFormDefaults {
    return {
      id: null,
    };
  }
}
