import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Opponent from './opponent';
import OpponentDetail from './opponent-detail';
import OpponentUpdate from './opponent-update';
import OpponentDeleteDialog from './opponent-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={OpponentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={OpponentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={OpponentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Opponent} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={OpponentDeleteDialog} />
  </>
);

export default Routes;
