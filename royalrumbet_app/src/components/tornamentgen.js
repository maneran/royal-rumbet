import React ,{ Fragment} from 'react'
import styles from './../styles/table.module.css'

function torgen(props) {
    
    const torKeys = Object.keys(props)
    const table = torKeys.map((torId,b)=> {
       
        return (
                <tr key={b}>
                    <td>{props[torId].name}</td>
                    <td>{props[torId].type}</td>
                    <td>{props[torId].matchesamount}</td>
                    <td>{props[torId].matchesprog}</td>
                    <td>{props[torId].owner}</td>
                    <td>{props[torId].leadplayer}</td>
                    <td>{props[torId].leadscore}</td>
                    <td>{props[torId].yourscore}</td>
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
export default torgen
