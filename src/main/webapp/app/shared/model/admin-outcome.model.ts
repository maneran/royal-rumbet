import { IGame } from 'app/shared/model/game.model';
import { IUser } from 'app/shared/model/user.model';
import { ITournament } from 'app/shared/model/tournament.model';

export interface IAdminOutcome {
  id?: number;
  endOutcomeOpponentA?: string | null;
  endOutcomeOpponentB?: string | null;
  game?: IGame;
  user?: IUser;
  tournament?: ITournament;
}

export const defaultValue: Readonly<IAdminOutcome> = {};
