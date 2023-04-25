import { IPart, NewPart } from './part.model';

export const sampleWithRequiredData: IPart = {
  id: 39736,
  title: 'Director',
};

export const sampleWithPartialData: IPart = {
  id: 26408,
  title: 'Gloves cyan',
  description: 'Chair',
  codeTitle: 'Coordinator',
  code: 'maroon Missouri New',
  result: 'Directives generating',
  additional: 'Saint Handcrafted reboot',
};

export const sampleWithFullData: IPart = {
  id: 48079,
  title: 'invoice',
  description: 'Cotton function real-time',
  question: 'Garden Rhode',
  codeTitle: 'Representative',
  codeDescription: 'Guernsey',
  code: 'Self-enabling Iowa Principal',
  fullCode: 'Fresh',
  result: 'Chicken enhance Computers',
  additional: 'driver Yemen',
};

export const sampleWithNewData: NewPart = {
  title: 'Product',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
