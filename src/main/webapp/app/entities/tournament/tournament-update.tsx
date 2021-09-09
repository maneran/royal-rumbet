import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './tournament.reducer';
import { ITournament } from 'app/shared/model/tournament.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const TournamentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const tournamentEntity = useAppSelector(state => state.tournament.entity);
  const loading = useAppSelector(state => state.tournament.loading);
  const updating = useAppSelector(state => state.tournament.updating);
  const updateSuccess = useAppSelector(state => state.tournament.updateSuccess);

  const handleClose = () => {
    props.history.push('/tournament' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateStart = convertDateTimeToServer(values.dateStart);
    values.dateEnd = convertDateTimeToServer(values.dateEnd);
    values.registrationEndDate = convertDateTimeToServer(values.registrationEndDate);

    const entity = {
      ...tournamentEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateStart: displayDefaultDateTime(),
          dateEnd: displayDefaultDateTime(),
          registrationEndDate: displayDefaultDateTime(),
        }
      : {
          ...tournamentEntity,
          dateStart: convertDateTimeFromServer(tournamentEntity.dateStart),
          dateEnd: convertDateTimeFromServer(tournamentEntity.dateEnd),
          registrationEndDate: convertDateTimeFromServer(tournamentEntity.registrationEndDate),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheepitApp.tournament.home.createOrEditLabel" data-cy="TournamentCreateUpdateHeading">
            Create or edit a Tournament
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="tournament-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="tournament-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Date Start"
                id="tournament-dateStart"
                name="dateStart"
                data-cy="dateStart"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Date End"
                id="tournament-dateEnd"
                name="dateEnd"
                data-cy="dateEnd"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField
                label="Registration End Date"
                id="tournament-registrationEndDate"
                name="registrationEndDate"
                data-cy="registrationEndDate"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/tournament" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">Back</span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp; Save
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default TournamentUpdate;
