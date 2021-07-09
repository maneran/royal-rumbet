import React, { Component, Fragment } from 'react'
import styles from './../styles/navbar.module.css'

export default class Topnavbar extends Component {
    render() {
        return (
            <Fragment>
                <div className={styles.topnav} id="myTopnav">
                    <a href="#home" className="active">Home</a>
                    <a href="#QuicNav">Quick-Nav</a>
                    <a href="#about">About</a>
                    <div className={styles.userlogged}>Sagi logged</div>
                  </div>
            </Fragment>
        )
    }
}
