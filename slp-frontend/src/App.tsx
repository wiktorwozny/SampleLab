import React from 'react';
import './App.css';
import SampleForm from './components/SampleForm';
import ReportDataForm from './components/ReportDataForm';
import ExaminationsList from "./components/ExaminationsList";
import {BrowserRouter, Route, Routes} from 'react-router-dom';
import SingleSamplePage from './pages/SingleSamplePage';
import SampleListPage from './pages/SampleListPage';
import ExaminationForm from "./components/ExaminationForm";
import Sidebar from "./components/Sidebar";
import AlertComponent from './components/AlertComponent';
import AlertsContext from './contexts/AlertsContext';
import BackupView from "./components/BackupView";
import DictionariesView from "./components/DictionariesView";
import LoginForm from './components/LoginForm';
import RegisterPage from './pages/RegisterPage';
import NoPermitionPage from './pages/NoPermitionPage';
import PrivateRoute from './components/PrivateRoute';
import CheckIsLogin from './components/CheckIsLogin';


function App() {
    return (
        <div className="App flex relative h-fit w-full">
            <AlertsContext>
                {/* <div className='fixed w-full top-2 z-2 justify-center'>
                    <AlertComponent/>
                </div> */}
                <CheckIsLogin>
                    <BrowserRouter>
                        <Sidebar/>
                        <div className='relative w-full min-h-screen'>
                            <div className='fixed w-full top-2 z-2 justify-center'>
                                <AlertComponent/>
                            </div>
                            <Routes>
                                <Route path='/' element={<SampleListPage/>}/>
                                <Route path='/addSample' element={<SampleForm/>}/>
                                <Route path='/sample/:sampleId' element={<SingleSamplePage/>}/>
                                <Route path='/sample/addReportData/:sampleId' element={<ReportDataForm/>}/>
                                <Route path='/sample/manageExaminations/:sampleId' element={<ExaminationsList/>}/>
                                <Route path='/sample/manageExaminations/:sampleId/newExamination' element={<ExaminationForm/>}/>
                                <Route path='/sample/manageExaminations/:sampleId/newExamination/:examinationId' element={<ExaminationForm/>}/>
                                <Route path='/backup' element={<BackupView/>}/>
                                <Route path='/dictionary' element={<DictionariesView/>}/>
                                <Route path='/login' element={<LoginForm/>}/>
                                <Route path='/register' element={
                                    <PrivateRoute>
                                        <RegisterPage/>
                                    </PrivateRoute>
                                }/>
                            </Routes>
                        </div>
                    </BrowserRouter>
                </CheckIsLogin>
                {/* <NoPermitionPage/> */}
                {/* <ReportDataForm/> */}
            </AlertsContext>
        </div>
    );
}

export default App;
