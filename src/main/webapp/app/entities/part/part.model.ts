import { ITheme } from 'app/entities/theme/theme.model';
import { IMedia } from 'app/entities/media/media.model';

export interface IPart {
  id: number;
  title?: string | null;
  description?: string | null;
  question?: string | null;
  codeTitle?: string | null;
  codeDescription?: string | null;
  code?: string | null;
  fullCode?: string | null;
  result?: string | null;
  additional?: string | null;
  theme?: Pick<ITheme, 'id'> | null;
  media?: Pick<IMedia, 'id'> | null;
}

export type NewPart = Omit<IPart, 'id'> & { id: null };
