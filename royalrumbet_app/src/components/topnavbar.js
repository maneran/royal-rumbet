import React, { Component, Fragment } from 'react'

import styles from './../styles/navbar.module.css'
import { Link } from "react-router-dom";

export default class Topnavbar extends Component {


    render() {
        return (
            <Fragment>
                <div className={styles.topnav} id="myTopnav">
                    <Link to="/">Home</Link>
                    {/* <a href="#home" className="active" onClick={this.btnHandler}>Home</a> */}
                    <a href="#QuicNav">Quick-Nav</a>
                    <a href="#about">About</a>
                    <div className={styles.userlogged}>Sagi logged</div>
                  </div>
            </Fragment>
        )
    }
}
