import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './user-outcome.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';
import { IUserOutcome } from 'app/shared/model/user-outcome.model';

export const UserOutcomeDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();
  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userOutcomeEntity = useAppSelector(state => state.userOutcome.entity);

  const { match } = props;

  return (
    <Row>
      <Col md="8">
        <h2 data-cy="userOutcomeDetailsHeading">UserOutcome</h2>
        <dl className="jh-entity-details">
          {/*<dt>*/}
          {/*  <span id="id">ID</span>*/}
          {/*</dt>*/}
          {/*<dd>{userOutcomeEntity.id}</dd>*/}
          <dt>
            <span id="endOutcomeOpponentA">End Outcome Opponent A</span>
          </dt>
          <dd>{userOutcomeEntity.endOutcomeOpponentA}</dd>
          <dt>
            <span id="endOutcomeOpponentB">End Outcome Opponent B</span>
          </dt>
          <dd>{userOutcomeEntity.endOutcomeOpponentB}</dd>
          <dt>Game</dt>
          <dd>{userOutcomeEntity.game ? userOutcomeEntity.game.name : ''}</dd>
          <dt>User</dt>
          <dd>{userOutcomeEntity.user ? userOutcomeEntity.user.login : ''}</dd>
          <dt>Tournament</dt>
          <dd>{userOutcomeEntity.tournament ? userOutcomeEntity.tournament.name : ''}</dd>
        </dl>
        <Button tag={Link} to="/user-outcome" replace color="info" data-cy="entityDetailsBackButton">
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

export default UserOutcomeDetail;
