import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './admin-outcome.reducer';

export const AdminOutcomeDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const adminOutcomeEntity = useAppSelector(state => state.adminOutcome.entity);
  const updateSuccess = useAppSelector(state => state.adminOutcome.updateSuccess);

  const handleClose = () => {
    props.history.push('/admin-outcome');
  };

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    const id = adminOutcomeEntity.game.id + '_' + adminOutcomeEntity.user.id + '_' + adminOutcomeEntity.tournament.id;
    dispatch(deleteEntity(id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="adminOutcomeDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="sheepitApp.adminOutcome.delete.question">Are you sure you want to delete this AdminOutcome?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-adminOutcome" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default AdminOutcomeDeleteDialog;
