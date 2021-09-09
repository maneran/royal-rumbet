import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Group from './group';
import Tournament from './tournament';
import Opponent from './opponent';
import Game from './game';
import GameConfig from './game-config';
import AdminOutcome from './admin-outcome';
import UserOutcome from './user-outcome';
import Score from './score';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}group`} component={Group} />
      <ErrorBoundaryRoute path={`${match.url}tournament`} component={Tournament} />
      <ErrorBoundaryRoute path={`${match.url}opponent`} component={Opponent} />
      <ErrorBoundaryRoute path={`${match.url}game`} component={Game} />
      <ErrorBoundaryRoute path={`${match.url}game-config`} component={GameConfig} />
      <ErrorBoundaryRoute path={`${match.url}admin-outcome`} component={AdminOutcome} />
      <ErrorBoundaryRoute path={`${match.url}user-outcome`} component={UserOutcome} />
      <ErrorBoundaryRoute path={`${match.url}score`} component={Score} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
