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
import { getEntity, updateEntity, createEntity, reset } from './admin-outcome.reducer';
import { IAdminOutcome } from 'app/shared/model/admin-outcome.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AdminOutcomeUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const games = useAppSelector(state => state.game.entities);
  const users = useAppSelector(state => state.userManagement.users);
  const tournaments = useAppSelector(state => state.tournament.entities);
  const adminOutcomeEntity = useAppSelector(state => state.adminOutcome.entity);
  const loading = useAppSelector(state => state.adminOutcome.loading);
  const updating = useAppSelector(state => state.adminOutcome.updating);
  const updateSuccess = useAppSelector(state => state.adminOutcome.updateSuccess);

  const handleClose = () => {
    props.history.push('/admin-outcome');
  };

  useEffect(() => {
    if (!isNew) {
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
      ...adminOutcomeEntity,
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
          ...adminOutcomeEntity,
          gameId: adminOutcomeEntity?.game?.id,
          userId: adminOutcomeEntity?.user?.id,
          tournamentId: adminOutcomeEntity?.tournament?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="sheepitApp.adminOutcome.home.createOrEditLabel" data-cy="AdminOutcomeCreateUpdateHeading">
            Create or edit a AdminOutcome
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {/*{!isNew ? (*/}
              {/*  <ValidatedField name="id" required readOnly id="admin-outcome-id" label="ID" validate={{ required: true }} />*/}
              {/*) : null}*/}
              <ValidatedField
                label="End Outcome Opponent A"
                id="admin-outcome-endOutcomeOpponentA"
                name="endOutcomeOpponentA"
                data-cy="endOutcomeOpponentA"
                type="text"
                validate={{
                  pattern: { value: /^[0-9]*$/, message: "This field should follow pattern for '^[0-9]*.." },
                }}
              />
              <ValidatedField
                label="End Outcome Opponent B"
                id="admin-outcome-endOutcomeOpponentB"
                name="endOutcomeOpponentB"
                data-cy="endOutcomeOpponentB"
                type="text"
                validate={{
                  pattern: { value: /^[0-9]*$/, message: "This field should follow pattern for '^[0-9]*.." },
                }}
              />
              <ValidatedField id="admin-outcome-game" name="gameId" data-cy="game" label="Game" type="select" required>
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
              <ValidatedField id="admin-outcome-user" name="userId" data-cy="user" label="User" type="select" required>
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
              <ValidatedField
                id="admin-outcome-tournament"
                name="tournamentId"
                data-cy="tournament"
                label="Tournament"
                type="select"
                required
              >
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
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/admin-outcome" replace color="info">
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

export default AdminOutcomeUpdate;
