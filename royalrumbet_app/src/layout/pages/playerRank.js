import React, { Component, Fragment } from 'react'
import { Link } from "react-router-dom";

import dummyData3 from './../../dummyData/dummy_player_rank'
import TableGen from '../../components/table_gen'


const playerRankCol = { 
    playername : 'Player Name',
    score: 'Score',
   }

const rows = dummyData3

class PlayerRank extends Component {
    render() {
        console.log(this.props)
        const torNames = this.props.location.state.torNames
        return (
            <Fragment>
                <h2>Player Ranking Page <Link to={{pathname: `torna`+ torNames,
                                                    state: {torNames: torNames}
                                                 }} 
                                                 torNames>swipe right</Link></h2>  
                <br/>
                <div>
                player ranking table
                <br/>
                <TableGen data={{columns: playerRankCol ,
                                 rows: {[torNames]: rows.tordata[torNames]}}}/>
            </div>
            </Fragment>  
        )
    }
}

export default PlayerRank;
