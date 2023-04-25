import { ICourse, NewCourse } from './course.model';

export const sampleWithRequiredData: ICourse = {
  id: 59109,
  name: 'multi-byte California',
};

export const sampleWithPartialData: ICourse = {
  id: 77622,
  name: 'quantifying Unbranded',
};

export const sampleWithFullData: ICourse = {
  id: 34332,
  name: 'redundant',
};

export const sampleWithNewData: NewCourse = {
  name: 'hacking solution Rhode',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
