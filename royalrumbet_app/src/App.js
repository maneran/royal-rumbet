import './App.css';

import Welcome from './layout/pages/welcome'
import Topnavbar from './components/topnavbar'

function App() {
  return (
    <div className="App">
      <Topnavbar/>
      <div class='App-header'>
        <Welcome/>
      </div>
     
    </div>
  );
}

export default App;
