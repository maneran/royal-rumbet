import React, { useState, useEffect } from 'react';
import InfiniteScroll from 'react-infinite-scroller';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities, reset } from './admin-outcome.reducer';
import { IAdminOutcome } from 'app/shared/model/admin-outcome.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from 'app/shared/util/pagination.constants';
import { overridePaginationStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const AdminOutcome = (props: RouteComponentProps<{ url: string }>) => {
  const dispatch = useAppDispatch();

  const [paginationState, setPaginationState] = useState(
    overridePaginationStateWithQueryParams(getSortState(props.location, ITEMS_PER_PAGE, 'id'), props.location.search)
  );
  const [sorting, setSorting] = useState(false);

  const adminOutcomeList = useAppSelector(state => state.adminOutcome.entities);
  const loading = useAppSelector(state => state.adminOutcome.loading);
  const totalItems = useAppSelector(state => state.adminOutcome.totalItems);
  const links = useAppSelector(state => state.adminOutcome.links);
  const entity = useAppSelector(state => state.adminOutcome.entity);
  const updateSuccess = useAppSelector(state => state.adminOutcome.updateSuccess);

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
      <h2 id="admin-outcome-heading" data-cy="AdminOutcomeHeading">
        Admin Outcomes
        <div className="d-flex justify-content-end">
          <Button className="mr-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} /> Refresh List
          </Button>
          <Link to={`${match.url}/new`} className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp; Create new Admin Outcome
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
          {adminOutcomeList && adminOutcomeList.length > 0 ? (
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
                {adminOutcomeList.map((adminOutcome, i) => (
                  <tr key={`entity-${i}`} data-cy="entityTable">
                    {/*<td>*/}
                    {/*  <Button tag={Link} to={`${match.url}/${adminOutcome.id}`} color="link" size="sm">*/}
                    {/*    {adminOutcome.id}*/}
                    {/*  </Button>*/}
                    {/*</td>*/}
                    <td>{adminOutcome.endOutcomeOpponentA}</td>
                    <td>{adminOutcome.endOutcomeOpponentB}</td>
                    <td>{adminOutcome.game ? <Link to={`game/${adminOutcome.game.id}`}>{adminOutcome.game.name}</Link> : ''}</td>
                    <td>{adminOutcome.user ? adminOutcome.user.login : ''}</td>
                    <td>
                      {adminOutcome.tournament ? (
                        <Link to={`tournament/${adminOutcome.tournament.id}`}>{adminOutcome.tournament.name}</Link>
                      ) : (
                        ''
                      )}
                    </td>
                    <td className="text-right">
                      <div className="btn-group flex-btn-group-container">
                        <Button
                          tag={Link}
                          to={`${match.url}/${adminOutcome.game.id}_${adminOutcome.user.id}_${adminOutcome.tournament.id}`}
                          color="info"
                          size="sm"
                          data-cy="entityDetailsButton"
                        >
                          <FontAwesomeIcon icon="eye" /> <span className="d-none d-md-inline">View</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${adminOutcome.game.id}_${adminOutcome.user.id}_${adminOutcome.tournament.id}/edit`}
                          color="primary"
                          size="sm"
                          data-cy="entityEditButton"
                        >
                          <FontAwesomeIcon icon="pencil-alt" /> <span className="d-none d-md-inline">Edit</span>
                        </Button>
                        <Button
                          tag={Link}
                          to={`${match.url}/${adminOutcome.game.id}_${adminOutcome.user.id}_${adminOutcome.tournament.id}/delete`}
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
            !loading && <div className="alert alert-warning">No Admin Outcomes found</div>
          )}
        </InfiniteScroll>
      </div>
    </div>
  );
};

export default AdminOutcome;
