import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './admin-outcome.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AdminOutcomeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const adminOutcomeEntity = useAppSelector(state => state.adminOutcome.entity);

  const { match } = props;

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="adminOutcomeDetailsHeading">AdminOutcome</h2>
        <dl className="jh-entity-details">
          {/*<dt>*/}
          {/*  <span id="id">ID</span>*/}
          {/*</dt>*/}
          {/*<dd>{adminOutcomeEntity.id}</dd>*/}
          <dt>
            <span id="endOutcomeOpponentA">End Outcome Opponent A</span>
          </dt>
          <dd>{adminOutcomeEntity.endOutcomeOpponentA}</dd>
          <dt>
            <span id="endOutcomeOpponentB">End Outcome Opponent B</span>
          </dt>
          <dd>{adminOutcomeEntity.endOutcomeOpponentB}</dd>
          <dt>Game</dt>
          <dd>{adminOutcomeEntity.game ? adminOutcomeEntity.game.name : ''}</dd>
          <dt>User</dt>
          <dd>{adminOutcomeEntity.user ? adminOutcomeEntity.user.login : ''}</dd>
          <dt>Tournament</dt>
          <dd>{adminOutcomeEntity.tournament ? adminOutcomeEntity.tournament.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/admin-outcome" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`${match.url}/edit`} replace color="primary" data-cy="entityEditButton">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default AdminOutcomeDetail;
