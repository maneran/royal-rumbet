import React, { Component, Fragment } from 'react'
import { Link } from "react-router-dom";

class PlayerRank extends Component {
    render() {
        console.log(this.props)
        const torNames = this.props.location.state.torNames
        return (
            <Fragment>
                <h2>Player Ranking Page <Link to={{pathname: `torna`+torNames,
                                                    state: {torNames: torNames}
                                                 }} 
                                                 torNames>swipe right</Link></h2>  
                <br/>
                <div>
                player ranking table
            </div>
            </Fragment>  
        )
    }
}

export default PlayerRank;
