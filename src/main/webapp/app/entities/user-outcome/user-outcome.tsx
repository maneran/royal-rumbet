import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, reset } from './user-outcome.reducer';
import { IUserOutcome } from 'app/shared/model/user-outcome.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const UserOutcome = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const userOutcomeList = useAppSelector(state => state.userOutcome.entities);
  const loading = useAppSelector(state => state.userOutcome.loading);
  const totalItems = useAppSelector(state => state.userOutcome.totalItems);
  const links = useAppSelector(state => state.userOutcome.links);
  const entity = useAppSelector(state => state.userOutcome.entity);
  const updateSuccess = useAppSelector(state => state.userOutcome.updateSuccess);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        page: paginationState.activePage - 1,
        size: paginationState.itemsPerPage,
        sort: `${paginationState.sort},${paginationState.order}`,
      })
    );
  };

  const resetAll = () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
    });
    dispatch(getEntities({}));
  };

  useEffect(() => {
    resetAll();
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      resetAll();
    }
  }, [updateSuccess]);

  useEffect(() => {
    getAllEntities();
  }, [paginationState.activePage]);

  const handleLoadMore = () => {
    if ((window as any).pageYOffset > 0) {
      setPaginationState({
        ...paginationState,
        activePage: paginationState.activePage + 1,
      });
    }
  };

  useEffect(() => {
    if (sorting) {
      getAllEntities();
      setSorting(false);
    }
  }, [sorting]);

  const sort = p => () => {
    dispatch(reset());
    setPaginationState({
      ...paginationState,
      activePage: 1,
      order: paginationState.order === ASC ? DESC : ASC,
      sort: p,
    });
    setSorting(true);
  };

  const handleSyncList = () => {
    resetAll();
  };

  const { match } = props;

  return (
    <div>
      <h2 id="user-outcome-heading" data-cy="UserOutcomeHeading">
        User Outcomes
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new User Outcome
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        <InfiniteScroll
          pageStart={paginationState.activePage}
          loadMore={handleLoadMore}
          hasMore={paginationState.activePage - 1 < links.next}
          loader={<div className="loader">Loading ...</div>}
          threshold={0}
          initialLoad={false}
        >
          {userOutcomeList && userOutcomeList.length > 0 ? (
            <Table responsive>
              <thead>
                <tr>
                  {/*<th className="hand" onClick={sort('id')}>*/}
                  {/*  ID <FontAwesomeIcon icon="sort" />*/}
                  {/*</th>*/}
                  <th className="hand" onClick={sort('endOutcomeOpponentA')}>
                    End Outcome Opponent A <FontAwesomeIcon icon="sort" />
                  </th>
                  <th className="hand" onClick={sort('endOutcomeOpponentB')}>
                    End Outcome Opponent B <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Game <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    User <FontAwesomeIcon icon="sort" />
                  </th>
                  <th>
                    Tournament <FontAwesomeIcon icon="sort" />
                  </th>
                  <th />
                </tr>
              </thead>
              <tbody>
                {userOutcomeList.map((userOutcome, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    {/*<td>*/}
                    {/*  <Button tag={Link} to={`${match.url}/${userOutcome.id}`} color="link" size="sm">*/}
                    {/*    {userOutcome.id}*/}
                    {/*  </Button>*/}
                    {/*</td>*/}
                    <td>{userOutcome.endOutcomeOpponentA}</td>
                    <td>{userOutcome.endOutcomeOpponentB}</td>
                    <td>{userOutcome.game ? <Link to={`game/${userOutcome.game.id}`}>{userOutcome.game.name}</Link> : ''}</td>
                    <td>{userOutcome.user ? userOutcome.user.login : ''}</td>
                    <td>
                      {userOutcome.tournament ? (
                        <Link to={`tournament/${userOutcome.tournament.id}`}>{userOutcome.tournament.name}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`${match.url}/${userOutcome.game.id}_${userOutcome.user.id}_${userOutcome.tournament.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${userOutcome.game.id}_${userOutcome.user.id}_${userOutcome.tournament.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${userOutcome.game.id}_${userOutcome.user.id}_${userOutcome.tournament.id}/delete`}
                          color="danger"
                          size="sm"
                          data-cy="entityDeleteButton"
                        >
                          <FontAwesomeIcon icon="trash" /> <span className="d-none d-md-inline">Delete</span>
                        </Button>
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </Table>
          ) : (
            !loading && <div className="alert alert-warning">No User Outcomes found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default UserOutcome;
