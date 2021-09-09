import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IGame } from 'app/shared/model/game.model';
import { getEntities as getGames } from 'app/entities/game/game.reducer';
import { IUser } from 'app/shared/model/user.model';
import { getUsers } from 'app/shared/reducers/user-management';
import { ITournament } from 'app/shared/model/tournament.model';
import { getEntities as getTournaments } from 'app/entities/tournament/tournament.reducer';
import { getEntity, updateEntity, createEntity, reset } from './score.reducer';
import { IScore } from 'app/shared/model/score.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ScoreUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const games = useAppSelector(state => state.game.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const tournaments = useAppSelector(state => state.tournament.entities);
  const scoreEntity = useAppSelector(state => state.score.entity);
  const loading = useAppSelector(state => state.score.loading);
  const updating = useAppSelector(state => state.score.updating);
  const updateSuccess = useAppSelector(state => state.score.updateSuccess);

  const handleClose = () => {
    props.history.push('/score' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getGames({}));
    dispatch(getUsers({}));
    dispatch(getTournaments({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...scoreEntity,
      ...values,
      game: games.find(it => it.id.toString() === values.gameId.toString()),
      user: users.find(it => it.id.toString() === values.userId.toString()),
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
      ? {}
      : {
          ...scoreEntity,
          gameId: scoreEntity?.game?.id,
          userId: scoreEntity?.user?.id,
          tournamentId: scoreEntity?.tournament?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheepitApp.score.home.createOrEditLabel" data-cy="ScoreCreateUpdateHeading">
            Create or edit a Score
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {/*{!isNew ? <ValidatedField name="id" required readOnly id="score-id" label="ID" validate={{ required: true }} /> : null}*/}
              <ValidatedField label="Points" id="score-points" name="points" data-cy="points" type="text" />
              <ValidatedField id="score-game" name="gameId" data-cy="game" label="Game" type="select" required>
                <option value="" key="0" />
                {games
                  ? games.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.name}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="score-user" name="userId" data-cy="user" label="User" type="select" required>
                <option value="" key="0" />
                {users
                  ? users.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.login}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <FormText>This field is required.</FormText>
              <ValidatedField id="score-tournament" name="tournamentId" data-cy="tournament" label="Tournament" type="select" required>
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/score" replace color="info">
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

export default ScoreUpdate;
