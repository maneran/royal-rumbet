import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IOpponent } from 'app/shared/model/opponent.model';
import { getEntities as getOpponents } from 'app/entities/opponent/opponent.reducer';
import { ITournament } from 'app/shared/model/tournament.model';
import { getEntities as getTournaments } from 'app/entities/tournament/tournament.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { getEntity, updateEntity, createEntity, reset } from './game.reducer';
import { IGame } from 'app/shared/model/game.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const opponents = useAppSelector(state => state.opponent.entities);
  const tournaments = useAppSelector(state => state.tournament.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const gameEntity = useAppSelector(state => state.game.entity);
  const loading = useAppSelector(state => state.game.loading);
  const updating = useAppSelector(state => state.game.updating);
  const updateSuccess = useAppSelector(state => state.game.updateSuccess);

  const handleClose = () => {
    props.history.push('/game');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getOpponents({}));
    dispatch(getTournaments({}));
    dispatch(getUsers({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateStart = convertDateTimeToServer(values.dateStart);

    const entity = {
      ...gameEntity,
      ...values,
      users: mapIdList(values.users),
      opponentA: opponents.find(it => it.id.toString() === values.opponentAId.toString()),
      opponentB: opponents.find(it => it.id.toString() === values.opponentBId.toString()),
      tournament: tournaments.find(it => it.id.toString() === values.tournamentId.toString()),
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
        }
      : {
          ...gameEntity,
          dateStart: convertDateTimeFromServer(gameEntity.dateStart),
          type: 'SOCCER',
          // stageType: 'ROUND_ROBIN',
          opponentAId: gameEntity?.opponentA?.id,
          opponentBId: gameEntity?.opponentB?.id,
          tournamentId: gameEntity?.tournament?.id,
          users: gameEntity?.users?.map(e => e.id.toString()),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheepitApp.game.home.createOrEditLabel" data-cy="GameCreateUpdateHeading">
            Create or edit a Game
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? <ValidatedField name="id" required readOnly id="game-id" label="ID" validate={{ required: true }} /> : null}
              <ValidatedField label="Name" id="game-name" name="name" data-cy="name" type="text" />
              <ValidatedField
                label="Date Start"
                id="game-dateStart"
                name="dateStart"
                data-cy="dateStart"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
                validate={{
                  required: { value: true, message: 'This field is required.' },
                }}
              />
              <ValidatedField label="Game Type" id="game-type" name="gameType" data-cy="gameType" type="select">
                <option value="SOCCER">Soccer</option>
                <option value="BASKETBALL">Basketball</option>
                <option value="TENNIS">Tennis</option>
              </ValidatedField>
              <ValidatedField label="Stage Type" id="game-stageType" name="stageType" data-cy="stageType" type="select">
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
                <option value="EIGHTH_FINALS">Eighth-finals</option>
                <option value="QUARTER_FINALS">Quarterfinals</option>
                <option value="SWEET_SIXTEEN">Sweet Sixteen</option>
                <option value="ELITE_EIGHT">Elite Eight</option>
                <option value="SEMI_FINALS">Semifinals</option>
                <option value="FINAL_FOUR">Final Four</option>
                <option value="FINAL">Final</option>
                <option value="NATIONAL_CHAMPIONSHIP">National Championship</option>
              </ValidatedField>
              <ValidatedField id="game-opponentA" name="opponentAId" data-cy="opponentA" label="Opponent A" type="select" required>
                <option value="" key="0" />
                {opponents
                  ? opponents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="game-opponentB" name="opponentBId" data-cy="opponentB" label="Opponent B" type="select" required>
                <option value="" key="0" />
                {opponents
                  ? opponents.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="game-tournament" name="tournamentId" data-cy="tournament" label="Tournament" type="select" required>
                <option value="" key="0" />
                {tournaments
                  ? tournaments.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField label="User" id="game-user" data-cy="user" type="select" multiple name="users">
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/game" replace color="info">
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

export default GameUpdate;
