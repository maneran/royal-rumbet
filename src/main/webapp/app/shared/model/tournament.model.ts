import dayjs from 'dayjs';
import { IGame } from 'app/shared/model/game.model';

export interface ITournament {
  id?: number;
  name?: string;
  dateStart?: string;
  dateEnd?: string;
  registrationEndDate?: string;
  games?: IGame[] | null;
}

export const defaultValue: Readonly<ITournament> = {};
