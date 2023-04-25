import { ITheme } from 'app/entities/theme/theme.model';

export interface IMedia {
  id: number;
  title?: string | null;
  description?: string | null;
  photo?: string | null;
  video?: string | null;
  theme?: Pick<ITheme, 'id'> | null;
}

export type NewMedia = Omit<IMedia, 'id'> & { id: null };
