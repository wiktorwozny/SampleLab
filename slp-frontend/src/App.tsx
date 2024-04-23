import React from 'react';
import './App.css';
import SampleForm from './components/SampleForm';
import ReportDataForm from './components/ReportDataForm';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SingleSamplePage from './pages/SingleSamplePage';
import SampleListPage from './pages/SampleListPage';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<SampleListPage/>}/>
          <Route path='/addSample' element={<SampleForm/>}/>
          <Route path='/sample/:sampleId' element={<SingleSamplePage/>}/>
          <Route path='/sample/addReportData/:sampleId' element={<ReportDataForm/>}/>
        </Routes>
      </BrowserRouter>
      {/* <ReportDataForm/> */}
    </div>
  );
}

export default App;
