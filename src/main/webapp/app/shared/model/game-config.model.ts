import { GameType } from 'app/shared/model/enumerations/game-type.model';
import { GameStageType } from 'app/shared/model/enumerations/game-stage-type.model';
import { GameOutcome } from 'app/shared/model/enumerations/game-outcome.model';

export interface IGameConfig {
  id?: number;
  gameType?: GameType;
  gameStageType?: GameStageType;
  gameOutcome?: GameOutcome;
  points?: number;
}

export const defaultValue: Readonly<IGameConfig> = {};
