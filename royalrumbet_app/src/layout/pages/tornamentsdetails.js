import React, { Component, Fragment } from 'react'

import TableGen from '../../components/table_gen'
import dummyData2 from './../../dummyData/dummyData2'

const cols = { name : 'name',
               yourscore:  'yourscore'
             }


const rows = dummyData2

class Tornamentsdetails extends Component {

    constructor(props) {
        super(props);
        this.state = { counter: 0 };
    }

    render() {
        const torNames = this.props.match.params.id
        console.log(rows.tordata[torNames])

        return (
            <Fragment>
                <TableGen data={{columns: cols ,
                    rows: {[torNames]: rows.tordata[torNames]}}}/> 
            </Fragment>
        )
    }
}

export default Tornamentsdetails


