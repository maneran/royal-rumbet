import dayjs from 'dayjs';
import { IOpponent } from 'app/shared/model/opponent.model';
import { ITournament } from 'app/shared/model/tournament.model';
import { IUser } from 'app/shared/model/user.model';
import { GameType } from 'app/shared/model/enumerations/game-type.model';
import { GameStageType } from 'app/shared/model/enumerations/game-stage-type.model';

export interface IGame {
  id?: number;
  name?: string | null;
  dateStart?: string;
  gameType?: GameType;
  stageType?: GameStageType;
  opponentA?: IOpponent;
  opponentB?: IOpponent;
  tournament?: ITournament;
  users?: IUser[] | null;
}

export const defaultValue: Readonly<IGame> = {};
