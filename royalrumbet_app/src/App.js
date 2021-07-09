import './App.css';

// import Login from './layout/pages/login'
import Topnavbar from './components/topnavbar'
import Tornaments from './layout/pages/Tornaments'

function App() {
  return (
    <div>
      <div className="App-header">
        <Topnavbar/>
        <br/>
        <br/>
          <h2>Main Page</h2>
      </div>
      <div className='App-body'>
          {/* <Login/> */}
          <Tornaments/>
      </div>
      <div className='App-foot'>
      </div>
    </div>
  );
}

export default App;
