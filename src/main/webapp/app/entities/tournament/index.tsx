import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Tournament from './tournament';
import TournamentDetail from './tournament-detail';
import TournamentUpdate from './tournament-update';
import TournamentDeleteDialog from './tournament-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={TournamentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={TournamentUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={TournamentDetail} />
      <ErrorBoundaryRoute path={match.url} component={Tournament} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={TournamentDeleteDialog} />
  </>
);

export default Routes;
