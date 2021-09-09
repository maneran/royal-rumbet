import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import group from 'app/entities/group/group.reducer';
// prettier-ignore
import tournament from 'app/entities/tournament/tournament.reducer';
// prettier-ignore
import opponent from 'app/entities/opponent/opponent.reducer';
// prettier-ignore
import game from 'app/entities/game/game.reducer';
// prettier-ignore
import gameConfig from 'app/entities/game-config/game-config.reducer';
// prettier-ignore
import adminOutcome from 'app/entities/admin-outcome/admin-outcome.reducer';
// prettier-ignore
import userOutcome from 'app/entities/user-outcome/user-outcome.reducer';
// prettier-ignore
import score from 'app/entities/score/score.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

const rootReducer = {
  authentication,
  applicationProfile,
  administration,
  userManagement,
  group,
  tournament,
  opponent,
  game,
  gameConfig,
  adminOutcome,
  userOutcome,
  score,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
};

export default rootReducer;
