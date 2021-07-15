import React, { Component, Fragment } from 'react'

import TableGen from '../../components/table_gen'
import dummyData2 from '../../dummyData/dummy_matches'
import styles from './../../styles/tornament.module.css'

const cols = { teamA: 'Team A',
               teamB: 'Team B',
               dateStart: 'Start Fate',
               scoreTeanA: 'Team A Score',
               scoreTeanB: 'Team B Score',
               score: 'Score',
             }


const rows = dummyData2

class Matches extends Component {

    constructor(props) {
        super(props);
        this.state = { counter: 0 };
    }

    render() {
        const torNames = this.props.match.params.id
        console.log(rows.tordata[torNames])

        return (
            <Fragment>
                <h2>Matches Page</h2>  
                <br/>
                <div className={styles.tornament_container}>
                    <TableGen data={{columns: cols ,
                        rows: {[torNames]: rows.tordata[torNames]}}}/>
                </div> 
            </Fragment>
        )
    }
}

export default Matches


