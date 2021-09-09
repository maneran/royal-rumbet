import { IUser } from 'app/shared/model/user.model';

export interface IGroup {
  id?: number;
  name?: string;
  users?: IUser[] | null;
}

export const defaultValue: Readonly<IGroup> = {};
