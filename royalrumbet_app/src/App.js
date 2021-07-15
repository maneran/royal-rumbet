import './App.css';

import { BrowserRouter as Router , Route , Switch} from "react-router-dom";

// import Login from './layout/pages/login'
import Topnavbar from './components/topnavbar'
import Tornaments from './layout/pages/Tornaments'
import Matches from './layout/pages/matches'

function App() {
  return (
    <Router>
    <div>
      <div className="App-header">
        <Topnavbar/>
      </div>
      <div className='App-body'>
        <br/>
        <Switch>
           <Route exact path="/" component={Tornaments}></Route>
           <Route exact path="/torna:id" component={Matches}></Route>
         </Switch>
          {/* <Login/> */}
      </div>
      <div className='App-foot'>
      </div>
    </div>
    </Router>
  );
}

export default App;
