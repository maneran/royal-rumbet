import React, { Component , Fragment} from 'react'

// import axios from 'axios';
import TableGen from '../../components/table_gen'
import styles from './../../styles/tornament.module.css'
import Filterbar from './../../components/torfilterbat'
import dummyData from './../../dummyData/dummy_tornamets'

const tornamentTable = { 
                         name : 'name',
                         type: 'type',
                         matchesamount: 'match #num',
                         matchesprog: 'match rate',
                         owner: 'owner',
                         leadplayer: 'top player',
                         leadscore: 'top player score',
                         yourscore:  'yourscore'
                        }

export default class Tornaments extends Component {
   
    /*
        state will be pulled from api
        will contain user details 
        tormanent details per tornamet
    */

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
        console.log(dummyData);        
    }

    // componentDidUpdate(prevProps,prevState) {
    //     console.log(prevState.userId,prevState.userId !== undefined)
    //     if (prevState.userId !== undefined && prevState.userId !== this.state.userId) {
    //         console.log(prevState.userId,this.state.userId)
    //     }
    // }
   
    render() {
        return (
            <Fragment>
              <h2>Contests Page</h2>  
              <br/>
              <div className={styles.tornament_container}>
                <Filterbar/>
                <TableGen data={{columns: tornamentTable ,
                                 rows: dummyData.tordata}}/>
              </div>
            </Fragment>
        )
    }
}
