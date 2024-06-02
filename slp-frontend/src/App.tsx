import React from 'react';
import './App.css';
import SampleForm from './components/SampleForm';
import ReportDataForm from './components/ReportDataForm';
import ExaminationsList from "./components/ExaminationsList";
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import SingleSamplePage from './pages/SingleSamplePage';
import SampleListPage from './pages/SampleListPage';
import ExaminationForm from "./components/ExaminationForm";
import AddressForm from './components/AddressForm';

function App() {
  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<SampleListPage/>}/>
          <Route path='/addSample' element={<SampleForm/>}/>
          <Route path='/sample/:sampleId' element={<SingleSamplePage/>}/>
          <Route path='/sample/addReportData/:sampleId' element={<ReportDataForm/>}/>
          <Route path='/sample/manageExaminations/:sampleId' element={<ExaminationsList/>}/>
          <Route path='/sample/manageExaminations/:sampleId/newExamination' element={<ExaminationForm/>}/>
          <Route path='/sample/manageExaminations/:sampleId/newExamination/:examinationId' element={<ExaminationForm/>}/>
        </Routes>
      </BrowserRouter>
      {/* <AddressForm></AddressForm> */}
      {/* <ReportDataForm/> */}
    </div>
  );
}

export default App;
