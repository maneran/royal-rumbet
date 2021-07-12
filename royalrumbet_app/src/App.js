import './App.css';

import { BrowserRouter as Router , Route , Switch} from "react-router-dom";

// import Login from './layout/pages/login'
import Topnavbar from './components/topnavbar'
import Tornaments from './layout/pages/Tornaments'
import Tornamentsdetails from './layout/pages/tornamentsdetails'

function App() {
  return (
    <Router>
    <div>
      <div className="App-header">
        <Topnavbar/>
        <br/>
        <br/>
          <h2>Main Page</h2>
      </div>
      <div className='App-body'>
        <Switch>
          <Route exact path="/" component={Tornaments}></Route>
          <Route path="/torna:name" component={Tornamentsdetails}></Route>
        </Switch>
          {/* <Login/> */}
          {/* <Tornaments/> */}
      </div>
      <div className='App-foot'>
      </div>
    </div>
    </Router>
  );
}

export default App;
