import { OpponentType } from 'app/shared/model/enumerations/opponent-type.model';

export interface IOpponent {
  id?: number;
  name?: string;
  description?: string | null;
  opponentType?: OpponentType;
  numberOfPlayers?: number | null;
}

export const defaultValue: Readonly<IOpponent> = {};
