import React ,{ Fragment } from 'react'

import { Link, useHistory } from "react-router-dom";

import styles from './../styles/table.module.css'
/**/
function TableGen(props) {
    
    /*  
        props should be:
        columns = { colname: desc }
        rows = {id : { colname: value }}
    */


    const th_vals = Object.keys(props.data.columns)
    const td_vals = Object.keys(props.data.rows)
    const history = useHistory();
    console.log(th_vals)
    console.log(td_vals)
    const tds = td_vals.map((rowid,idx1)=> {

        let item_values = props.data.rows[Number(rowid)]
        let listthis = item_values
        //this will allos dynamic data strucrue 
        //that can be handled by this generator
        //cann get {} or [{}]
        if (Array.isArray(item_values)) {
            listthis = item_values
        }
        else {
            listthis = [item_values]
        } 
        let std_vals_jsx = listthis.map((obj,idx3) => {
            
            let td_vals_jsx = th_vals.map((colname,idx2) => {
                // need a better error handle
                // also consider using redirect
                //also - consider using route in the rh and td it self
                // try {
                //     (() => itemAmnt)()
                //     // (() => props.data.rows[Number(rowid)][colname])()
                // }
                // catch {

                //     (() => {alert('no such Patch');
                //             history.push('/');
                //             })()
                // }
                // console.log(obj[colname]);
                console.log(props.data.linkto ,  {id: rowid})
                return (
                    
                    // <td key={idx2}><Link to={{pathname:  props.data.linkto , state: {id: rowid}}}>{obj[colname]}</Link></td>
                     <td key={idx2}>{obj[colname]}</td>
                ) 
            })
            /* ADD GET PARAMS FROM LOCATION - IF ACCESS IS FROM URK IT SELF */
            /*will add a change route path to the url to generante the tornament details*/
            return (
                    <tr key={idx1+idx3} id={rowid} onClick={() => history.push('/' + props.data.linkto , {id: rowid})} > 
                       {/* <tr key={idx1+idx3} id={rowid}> */}
                          {td_vals_jsx}
                     </tr>
            )
        })
        return (
                std_vals_jsx
        )
  })  

    const ths = th_vals.map((coln, idx3) => {
        return (
            <th key={idx3}>{props.data.columns[coln]}</th>
        )
    })

    return (
        
        <Fragment>
            <table className={styles.table}>
                <thead>
                <tr>
                   {ths}
                </tr>
                </thead>
                <tbody>
                    {tds}
                </tbody>
                     
            </table>
        </Fragment>
    )
}
export default TableGen
