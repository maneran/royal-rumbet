import React, { Component, Fragment } from 'react'
import styles from './../styles/navbar.module.css'

export default class Topnavbar extends Component {
    render() {
        return (
            <Fragment>
                <div class={styles.topnav} id="myTopnav">
                  <a href="#home" class="active">Home</a>
                  <a href="#Account">Account</a>
                  <a href="#Tournaments">Tournaments</a>
                  <a href="#about">About</a>
                  </div>
            </Fragment>
        )
    }
}
