import React, { Component , Fragment} from 'react'

// import axios from 'axios';
import Torgen from '../../components/tornamentgen'
import styles from './../../styles/tornament.module.css'
import Filterbar from './../../components/torfilterbat'

export default class Tornaments extends Component {
   
    /*
        state will be pulled from api
        will contain user details 
        tormanent details per tornamet
    */
    state = {
        userId: 'sagi',
        tordata: {
            '123' : {
                    name: 'Euro24',
                    type: 'soccer',
                    matchesamount: 20,
                    matchesprog: '5/20',
                    owner: 'sagi',
                    leadplayer: 'manela',
                    leadscore: 40,
                    yourscore: 35,
            },
            '345' : {
                    name: 'RolandG',
                    type: 'Tennis',
                    matchesamount: 20,
                    matchesprog: '5/20',
                    owner: 'Ran',
                    leadplayer: 'manela',
                    leadscore: 40,
                    yourscore: 35,
            },
            '678' : {
                name: 'NBA',
                type: 'BasketBall',
                matchesamount: 20,
                matchesprog: '5/20',
                owner: 'Farkash',
                leadplayer: 'manela',
                leadscore: 40,
                yourscore: 35,
            },
            '901' : {
                name: 'Euro24',
                type: 'soccer',
                matchesamount: 20,
                matchesprog: '5/20',
                owner: 'Lavi',
                leadplayer: 'manela',
                leadscore: 40,
                yourscore: 35,
            },
        }
    }

    printit = (str) => {
        console.log('component mounted: ', str)
        this.setState({userId: 'sagiMounted',
                      torId: ['1234','456']})
    }

    componentDidMount = () => {
        /*
            call api for table userTornamnets
            and update the state
            user id from logged user
            tor list from the table
        */
        // axios.get("https://api.agify.io" , {name: 'michael'})
        //    .then(res => {
        //         const persons = res.data;
        //         this.setState({ persons });
        //         console.log(res.data)})             
    }

    componentDidUpdate(prevProps,prevState) {
        console.log(prevState.userId,prevState.userId !== undefined)
        if (prevState.userId !== undefined && prevState.userId !== this.state.userId) {
            console.log(prevState.userId,this.state.userId)
        }
    }
   
    render() {
        return (
            <Fragment>
              {/* <button onClick={this.printit.bind('hohoho')}>add table</button> */}
              <div className={styles.tornament_container}>
                <Filterbar/>
                {Torgen(this.state.tordata)}
              </div>
            </Fragment>
        )
    }
}
