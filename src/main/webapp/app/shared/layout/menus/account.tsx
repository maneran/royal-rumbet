import React from 'react';
import MenuItem from 'app/shared/layout/menus/menu-item';
import { DropdownItem } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getLoginUrl } from 'app/shared/util/url-utils';
import { NavDropdown } from './menu-components';
// sagis - required for user login name below
import { useAppSelector } from 'app/config/store';
import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';

const accountMenuItemsAuthenticated = (
  <>
    <MenuItem icon="sign-out-alt" to="/logout" data-cy="logout">
      Sign out
    </MenuItem>
  </>
);

const accountMenuItems = (
  <>
    <NavItem>
      <NavLink tag={Link} to={"/" + getLoginUrl()} className="d-flex align-items-center active" data-cy="login">
        <FontAwesomeIcon icon="sign-in-alt"/>
        <span>Sign in</span>
      </NavLink>
    </NavItem>
  </>
);
// sagis - change to component + replaced button name with user name. if not connected - just use sign in
// no need for a dropdown with one options - too many clicks...
export const AccountMenu = ({ isAuthenticated = false }) => {
  const login_name = useAppSelector(state => state.authentication.account).login;
  const account = login_name === undefined ? true : false;
  
  return (
  <>
  {
    account ? accountMenuItems
    :
    <NavDropdown icon="user" name={login_name} id="account-menu" data-cy="accountMenu">
      {isAuthenticated ? accountMenuItemsAuthenticated : accountMenuItems}
    </NavDropdown>
    }
  </>
  )
};

export default AccountMenu;
