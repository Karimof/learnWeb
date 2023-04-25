import { ICourse } from 'app/entities/course/course.model';

export interface ITheme {
  id: number;
  title?: string | null;
  course?: Pick<ICourse, 'id'> | null;
}

export type NewTheme = Omit<ITheme, 'id'> & { id: null };
