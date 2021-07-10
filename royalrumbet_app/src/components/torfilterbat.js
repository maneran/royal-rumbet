import React, { Component } from 'react'

import styles from './../styles/filterbar.module.css'

class Filterbar extends Component {
    render() {
        return (
            <div className={styles.filterbar}> 
                    filter Bar:   
                    <button className={styles.filterbtn}>Ttype : dropdown</button>
                    <button className={styles.filterbtn}>by name: search bar</button>
                    <button className={styles.filterbtn}>sort by: check boxes</button>
                    <button className={styles.filterbtn}>active check box</button>
                    <br/>
            </div>
        )
    }
}

export default Filterbar; 