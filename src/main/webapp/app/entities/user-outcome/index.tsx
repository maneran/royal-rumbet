import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserOutcome from './user-outcome';
import UserOutcomeDetail from './user-outcome-detail';
import UserOutcomeUpdate from './user-outcome-update';
import UserOutcomeDeleteDialog from './user-outcome-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={UserOutcomeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={UserOutcomeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={UserOutcomeDetail} />
      <ErrorBoundaryRoute path={match.url} component={UserOutcome} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={UserOutcomeDeleteDialog} />
  </>
);

export default Routes;
