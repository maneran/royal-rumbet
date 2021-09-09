import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import AdminOutcome from './admin-outcome';
import AdminOutcomeDetail from './admin-outcome-detail';
import AdminOutcomeUpdate from './admin-outcome-update';
import AdminOutcomeDeleteDialog from './admin-outcome-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={AdminOutcomeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={AdminOutcomeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={AdminOutcomeDetail} />
      <ErrorBoundaryRoute path={match.url} component={AdminOutcome} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={AdminOutcomeDeleteDialog} />
  </>
);

export default Routes;
