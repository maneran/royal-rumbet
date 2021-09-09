import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './game-config.reducer';
import { IGameConfig } from 'app/shared/model/game-config.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameConfigUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const gameConfigEntity = useAppSelector(state => state.gameConfig.entity);
  const loading = useAppSelector(state => state.gameConfig.loading);
  const updating = useAppSelector(state => state.gameConfig.updating);
  const updateSuccess = useAppSelector(state => state.gameConfig.updateSuccess);

  const handleClose = () => {
    props.history.push('/game-config' + props.location.search);
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
      ...gameConfigEntity,
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
          ...gameConfigEntity,
          type: 'SOCCER',
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheepitApp.gameConfig.home.createOrEditLabel" data-cy="GameConfigCreateUpdateHeading">
            Create or edit a GameConfig
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="game-config-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Game Type" id="game-config-game-type" name="gameType" data-cy="gameType" type="select">
                <option value="SOCCER">Soccer</option>
                <option value="BASKETBALL">Basketball</option>
                <option value="TENNIS">Tennis</option>
              </ValidatedField>
              <ValidatedField
                label="Game Stage Type"
                id="game-config-game-stage-type"
                name="gameStageType"
                data-cy="gameStageType"
                type="select"
              >
                <option value="ROUND_ROBIN">Round-Robin</option>
                <option value="FIRST_FOUR">First Four</option>
                <option value="ROUND_FOUR">Round 4</option>
                <option value="FIRST_ROUND">1st round</option>
                <option value="SECOND_ROUND">2nd round</option>
                <option value="THIRD_ROUND">3rd round</option>
                <option value="ROUND_FIVE">Round 5</option>
                <option value="ROUND_SIX">Round 6</option>
                <option value="ROUND_SEVEN">Round 7</option>
                <option value="FOURTH_ROUND">4th round</option>
                <option value="FIFTH_ROUND">5th round</option>
                <option value="EIGHTH_FINALS">Eighth-finals</option>
                <option value="QUARTER_FINALS">Quarterfinals</option>
                <option value="SWEET_SIXTEEN">Sweet Sixteen</option>
                <option value="ELITE_EIGHT">Elite Eight</option>
                <option value="SEMI_FINALS">Semifinals</option>
                <option value="FINAL_FOUR">Final Four</option>
                <option value="FINAL">Final</option>
                <option value="NATIONAL_CHAMPIONSHIP">National Championship</option>
              </ValidatedField>
              <ValidatedField label="Game Outcome" id="game-config-game-outcome" name="gameOutcome" data-cy="gameOutcome" type="select">
                <option value="MISSED">Missed</option>
                <option value="HIT">Hit</option>
                <option value="SPECIAL">Special</option>
              </ValidatedField>
              <ValidatedField
                label="Points"
                id="game-config-points"
                name="points"
                data-cy="points"
                type="text"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                  min: { value: 0, message: 'This field should be at least 0.' },
                  max: { value: 20, message: 'This field cannot be more than 20.' },
                  validate: v => isNumber(v) || 'This field should be a number.',
                }}
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game-config" replace color="info">
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

export default GameConfigUpdate;
