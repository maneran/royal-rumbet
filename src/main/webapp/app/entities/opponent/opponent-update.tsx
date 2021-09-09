import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './opponent.reducer';
import { IOpponent } from 'app/shared/model/opponent.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const OpponentUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const opponentEntity = useAppSelector(state => state.opponent.entity);
  const loading = useAppSelector(state => state.opponent.loading);
  const updating = useAppSelector(state => state.opponent.updating);
  const updateSuccess = useAppSelector(state => state.opponent.updateSuccess);

  const handleClose = () => {
    props.history.push('/opponent' + props.location.search);
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
    const entity = {
      ...opponentEntity,
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
      ? {}
      : {
          ...opponentEntity,
          type: 'SINGLE',
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheepitApp.opponent.home.createOrEditLabel" data-cy="OpponentCreateUpdateHeading">
            Create or edit a Opponent
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="opponent-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField
                label="Name"
                id="opponent-name"
                name="name"
                data-cy="name"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Description" id="opponent-description" name="description" data-cy="description" type="text" />
              <ValidatedField label="Opponent Type" id="opponent-type" name="opponentType" data-cy="opponentType" type="select">
                <option value="SINGLE">Single</option>
                <option value="TEAM">Team</option>
              </ValidatedField>
              <ValidatedField
                label="Number Of Players"
                id="opponent-numberOfPlayers"
                name="numberOfPlayers"
                data-cy="numberOfPlayers"
                type="text"
                validate={{
                  min: { value: 1, message: 'This field should be at least 1.' },
                  max: { value: 11, message: 'This field cannot be more than 11.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/opponent" replace color="info">
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

export default OpponentUpdate;
