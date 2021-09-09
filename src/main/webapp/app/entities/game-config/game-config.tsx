import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities } from './game-config.reducer';
import { IGameConfig } from 'app/shared/model/game-config.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const GameConfig = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const gameConfigList = useAppSelector(state => state.gameConfig.entities);
  const loading = useAppSelector(state => state.gameConfig.loading);

  useEffect(() => {
    dispatch(getEntities({}));
  }, []);

  const handleSyncList = () => {
    dispatch(getEntities({}));
  };

  const { match } = props;

  return (
    <div>
      <h2 id="game-config-heading" data-cy="GameConfigHeading">
        Game Configs
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Game Config
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {gameConfigList && gameConfigList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>ID</th>
                <th>Game Type</th>
                <th>Game Stage Type</th>
                <th>Game Outcome</th>
                <th>Points</th>
                <th />
              </tr>
            </thead>
            <tbody>
              {gameConfigList.map((gameConfig, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`${match.url}/${gameConfig.id}`} color="link" size="sm">
                      {gameConfig.id}
                    </Button>
                  </td>
                  <td>{gameConfig.gameType}</td>
                  <td>{gameConfig.gameStageType}</td>
                  <td>{gameConfig.gameOutcome}</td>
                  <td>{gameConfig.points}</td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${gameConfig.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gameConfig.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${gameConfig.id}/delete`} color="danger" size="sm" data-cy="entityDeleteButton">
                        <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && <div className="alert alert-warning">No Game Configs found</div>
        )}
      </div>
    </div>
  );
};

export default GameConfig;
