import React, { useEffect } from 'react';
import { RouteComponentProps } from 'react-router-dom';
import { Modal, ModalHeader, ModalBody, ModalFooter, Button } from 'reactstrap';

import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { useAppDispatch, useAppSelector } from 'app/config/store';
import { getEntity, deleteEntity } from './user-outcome.reducer';

export const UserOutcomeDeleteDialog = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const userOutcomeEntity = useAppSelector(state => state.userOutcome.entity);
  const updateSuccess = useAppSelector(state => state.userOutcome.updateSuccess);

  const handleClose = () => {
    props.history.push('/user-outcome');
  };

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const confirmDelete = () => {
    const id = userOutcomeEntity.game.id + '_' + userOutcomeEntity.user.id + '_' + userOutcomeEntity.tournament.id;
    dispatch(deleteEntity(id));
  };

  return (
    <Modal isOpen toggle={handleClose}>
      <ModalHeader toggle={handleClose} data-cy="userOutcomeDeleteDialogHeading">
        Confirm delete operation
      </ModalHeader>
      <ModalBody id="sheepitApp.userOutcome.delete.question">Are you sure you want to delete this UserOutcome?</ModalBody>
      <ModalFooter>
        <Button color="secondary" onClick={handleClose}>
          <FontAwesomeIcon icon="ban" />
          &nbsp; Cancel
        </Button>
        <Button id="jhi-confirm-delete-userOutcome" data-cy="entityConfirmDeleteButton" color="danger" onClick={confirmDelete}>
          <FontAwesomeIcon icon="trash" />
          &nbsp; Delete
        </Button>
      </ModalFooter>
    </Modal>
  );
};

export default UserOutcomeDeleteDialog;
