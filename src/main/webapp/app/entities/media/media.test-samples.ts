import { IMedia, NewMedia } from './media.model';

export const sampleWithRequiredData: IMedia = {
  id: 70740,
};

export const sampleWithPartialData: IMedia = {
  id: 95019,
  description: 'Regional Dynamic Tuna',
  photo: 'red paradigms Tactics',
  video: 'orange Chair interfaces',
};

export const sampleWithFullData: IMedia = {
  id: 88881,
  title: 'Division platforms',
  description: 'Research transmitting',
  photo: 'Sol Concrete Incredible',
  video: 'Refined Handcrafted Soft',
};

export const sampleWithNewData: NewMedia = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
