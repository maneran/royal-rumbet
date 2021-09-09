import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';

import { NavDropdown } from './menu-components';

export const EntitiesMenu = props => (
  <NavDropdown icon="th-list" name="Entities" id="entity-menu" data-cy="entity" style={{ maxHeight: '80vh', overflow: 'auto' }}>
    <>{/* to avoid warnings when empty */}</>
    <MenuItem icon="asterisk" to="/group">
      Group
    </MenuItem>
    <MenuItem icon="asterisk" to="/tournament">
      Tournament
    </MenuItem>
    <MenuItem icon="asterisk" to="/opponent">
      Opponent
    </MenuItem>
    <MenuItem icon="asterisk" to="/game">
      Game
    </MenuItem>
    <MenuItem icon="asterisk" to="/game-config">
      Game Config
    </MenuItem>
    <MenuItem icon="asterisk" to="/admin-outcome">
      Admin Outcome
    </MenuItem>
    <MenuItem icon="asterisk" to="/user-outcome">
      User Outcome
    </MenuItem>
    <MenuItem icon="asterisk" to="/score">
      Score
    </MenuItem>
    {/* jhipster-needle-add-entity-to-menu - JHipster will add entities to the menu here */}
  </NavDropdown>
);
