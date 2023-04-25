import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import { ITheme, NewTheme } from '../theme.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITheme for edit and NewThemeFormGroupInput for create.
 */
type ThemeFormGroupInput = ITheme | PartialWithRequiredKeyOf<NewTheme>;

type ThemeFormDefaults = Pick<NewTheme, 'id'>;

type ThemeFormGroupContent = {
  id: FormControl<ITheme['id'] | NewTheme['id']>;
  title: FormControl<ITheme['title']>;
  course: FormControl<ITheme['course']>;
};

export type ThemeFormGroup = FormGroup<ThemeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class ThemeFormService {
  createThemeFormGroup(theme: ThemeFormGroupInput = { id: null }): ThemeFormGroup {
    const themeRawValue = {
      ...this.getFormDefaults(),
      ...theme,
    };
    return new FormGroup<ThemeFormGroupContent>({
      id: new FormControl(
        { value: themeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      title: new FormControl(themeRawValue.title, {
        validators: [Validators.required],
      }),
      course: new FormControl(themeRawValue.course),
    });
  }

  getTheme(form: ThemeFormGroup): ITheme | NewTheme {
    return form.getRawValue() as ITheme | NewTheme;
  }

  resetForm(form: ThemeFormGroup, theme: ThemeFormGroupInput): void {
    const themeRawValue = { ...this.getFormDefaults(), ...theme };
    form.reset(
      {
        ...themeRawValue,
        id: { value: themeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): ThemeFormDefaults {
    return {
      id: null,
    };
  }
}
