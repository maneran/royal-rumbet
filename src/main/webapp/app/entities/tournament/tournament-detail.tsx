import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { TextFormat } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './tournament.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TournamentDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const tournamentEntity = useAppSelector(state => state.tournament.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="tournamentDetailsHeading">Tournament</h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">ID</span>
          </dt>
          <dd>{tournamentEntity.id}</dd>
          <dt>
            <span id="name">Name</span>
          </dt>
          <dd>{tournamentEntity.name}</dd>
          <dt>
            <span id="dateStart">Date Start</span>
          </dt>
          <dd>
            {tournamentEntity.dateStart ? <TextFormat value={tournamentEntity.dateStart} type="date" format={APP_DATE_FORMAT} /> : null}
          </dd>
          <dt>
            <span id="dateEnd">Date End</span>
          </dt>
          <dd>{tournamentEntity.dateEnd ? <TextFormat value={tournamentEntity.dateEnd} type="date" format={APP_DATE_FORMAT} /> : null}</dd>
          <dt>
            <span id="registrationEndDate">Registration End Date</span>
          </dt>
          <dd>
            {tournamentEntity.registrationEndDate ? (
              <TextFormat value={tournamentEntity.registrationEndDate} type="date" format={APP_DATE_FORMAT} />
            ) : null}
          </dd>
        </dl>
        <Button tag={Link} to="/tournament" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" /> <span className="d-none d-md-inline">Back</span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/tournament/${tournamentEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
        </Button>
      </Col>
    </Row>
  );
};

export default TournamentDetail;
