import React, { Component } from 'react'


class Tornamentsdetails extends Component {

    constructor(props) {
        super(props);
        this.state = { counter: 0 };
    }

    
    render() {
        const torName = this.props.match.params.name
        console.log(this.props)
        return (
            <div>
               {/* {this.props.match.params} */}
               im this contest {torName}
            </div>
        )
    }
}

export default Tornamentsdetails


