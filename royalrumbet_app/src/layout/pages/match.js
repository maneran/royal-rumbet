import React, { Component } from 'react'


import styles from './../../styles/tornament.module.css'

class Match extends Component {
    render() {
        console.log(this.props)
        return (
            <div>
                hi
              <div className={styles.tornament_container}> 
                <div>team a</div>  
                <div>team b</div>
                <div>bet</div> 
                <div>score</div> 
                <div>player 1</div> 
                <div>player 2</div>   
             </div>  
            </div>
        )
    }
}

export default Match;