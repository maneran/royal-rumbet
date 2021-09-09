import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import {} from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './opponent.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OpponentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const opponentEntity = useAppSelector(state => state.opponent.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="opponentDetailsHeading">Opponent</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{opponentEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{opponentEntity.name}</dd>
          <dt>
            <span id="description">Description</span>
          </dt>
          <dd>{opponentEntity.description}</dd>
          <dt>
            <span id="opponentType">Opponent Type</span>
          </dt>
          <dd>{opponentEntity.opponentType}</dd>
          <dt>
            <span id="numberOfPlayers">Number Of Players</span>
          </dt>
          <dd>{opponentEntity.numberOfPlayers}</dd>
        </dl>
        <Button tag={Link} to="/opponent" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/opponent/${opponentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default OpponentDetail;
