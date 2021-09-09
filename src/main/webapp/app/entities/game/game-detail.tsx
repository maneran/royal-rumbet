import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './game.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const gameEntity = useAppSelector(state => state.game.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="gameDetailsHeading">Game</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{gameEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{gameEntity.name}</dd>
          <dt>
            <span id="dateStart">Date Start</span>
          </dt>
          <dd>{gameEntity.dateStart ? <TextFormat value={gameEntity.dateStart} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="type">Game Type</span>
          </dt>
          <dd>{gameEntity.gameType}</dd>
          <dt>
            <span id="stageType">Stage Type</span>
          </dt>
          <dd>{gameEntity.stageType}</dd>
          <dt>Opponent A</dt>
          <dd>{gameEntity.opponentA ? gameEntity.opponentA.name : ''}</dd>
          <dt>Opponent B</dt>
          <dd>{gameEntity.opponentB ? gameEntity.opponentB.name : ''}</dd>
          <dt>Tournament</dt>
          <dd>{gameEntity.tournament ? gameEntity.tournament.name : ''}</dd>
          <dt>User</dt>
          <dd>
            {gameEntity.users
              ? gameEntity.users.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.login}</a>
                    {gameEntity.users && i === gameEntity.users.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/game" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/game/${gameEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default GameDetail;
