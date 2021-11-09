import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';

import { Row, Col, Alert } from 'reactstrap';

import { getLoginUrl, REDIRECT_URL } from 'app/shared/util/url-utils';
import { useAppSelector } from 'app/config/store';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  useEffect(() => {
    const redirectURL = localStorage.getItem(REDIRECT_URL);
    if (redirectURL) {
      localStorage.removeItem(REDIRECT_URL);
      location.href = `${location.origin}${redirectURL}`;
    }
  });

  return (
    <Row>
      <Col md="3" className="pad">
        <span className="hipster rounded" />
      </Col>
      <Col md="9">
        <h2>Welcome to Sheepit !</h2>
        {/* <p className="lead">This is your homepage</p> */}
        {account && account.login ? (
          null
          // <div>
          //   <Alert color="success">You are logged in as user {account.login}.</Alert>
          // </div>
        ) : (
          <div>
            <Alert color="warning">
              If you want to
              <span>&nbsp;</span>
              <a href={getLoginUrl()} className="alert-link">
                sign in
              </a>
              , you can try the default accounts:
              <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
              <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
            </Alert>
          </div>
        )}
       
       {/* {sagis removed jhipster links} */}
      </Col>
    </Row>
  );
};

export default Home;
