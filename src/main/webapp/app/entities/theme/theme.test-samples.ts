import { ITheme, NewTheme } from './theme.model';

export const sampleWithRequiredData: ITheme = {
  id: 85695,
  title: 'hacking',
};

export const sampleWithPartialData: ITheme = {
  id: 48905,
  title: 'Cambridgeshire collaborative',
};

export const sampleWithFullData: ITheme = {
  id: 90437,
  title: 'interactive Cheese Mauritius',
};

export const sampleWithNewData: NewTheme = {
  title: 'Metal interface Credit',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
