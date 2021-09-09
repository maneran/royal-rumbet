import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import GameConfig from './game-config';
import GameConfigDetail from './game-config-detail';
import GameConfigUpdate from './game-config-update';
import GameConfigDeleteDialog from './game-config-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={GameConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={GameConfigUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={GameConfigDetail} />
      <ErrorBoundaryRoute path={match.url} component={GameConfig} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={GameConfigDeleteDialog} />
  </>
);

export default Routes;
