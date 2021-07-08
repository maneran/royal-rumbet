import './App.css';

import Login from './layout/pages/login'
import Topnavbar from './components/topnavbar'

function App() {
  return (
    <div className="App">
      <Topnavbar/>
      <div class='App-header'>
        Welcome to Royal Rumbet stupid Name App!
        <br/>
        <Login/>
      </div>
     
    </div>
  );
}

export default App;
