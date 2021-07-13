import React ,{ Fragment } from 'react'

import { useHistory   } from "react-router-dom";

import styles from './../styles/table.module.css'
/**/
function Torgen(props) {

    const torKeys = Object.keys(props.data)
    const history = useHistory();
    const table = torKeys.map((torId,idx)=> {
        let item = props.data[torId]

        /*will add a change route path to the url to generante the tornament details*/
        return (
                <tr key={idx} id={item.name} onClick={() => history.push('/torna' + item.name)} > 
                    <td>{item.name}</td>
                    <td>{item.type}</td>
                    <td>{item.matchesamount}</td>
                    <td>{item.matchesprog}</td>
                    <td>{item.owner}</td>
                    <td>{item.leadplayer}</td>
                    <td>{item.leadscore}</td>
                    <td>{item.yourscore}</td>
                </tr>
        )
    })


    return (
        <Fragment>
            <table className={styles.table}>
                <thead>
                <tr>
                    <th>name</th>
                    <th>type</th>
                    <th>match #num</th>
                    <th>match rate</th>
                    <th>ownner</th>
                    <th>top player</th>
                    <th>top player score</th>
                    <th>your score</th>
                </tr>
                </thead>
                <tbody>
                     {table}
                </tbody>
            </table>
        </Fragment>
    )
}
export default Torgen
