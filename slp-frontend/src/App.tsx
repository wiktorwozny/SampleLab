import React, {useState} from 'react';
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

import PrivateRoute from './components/PrivateRoute';
import CheckIsLogin from './components/CheckIsLogin';
import ProtocolReportDataForm from "./components/ProtocolReportDataForm";
import DictionariesView from "./components/dictionary/DictionariesView";
import LoginForm from "./components/LoginForm";
import RegisterPage from "./pages/RegisterPage";
import ClientDict from "./components/dictionary/client/ClientDict";
import IndicationDict from "./components/dictionary/indication/IndicationDict";
import CodeDict from "./components/dictionary/code/CodeDict";
import InspectionDict from "./components/dictionary/inspection/InspectionDict";
import SamplingStandardDict from "./components/dictionary/sampling-standard/SamplingStandardDict";
import ProductGroupDict from "./components/dictionary/product-group/ProductGroupDict";
import AssortmentDict from "./components/dictionary/assortment/AssortmentDict";
import ImportMethodsForm from './components/ImportMethodsForm';
import ChangePasswordForm from './components/ChangePasswordForm';

function App() {
    const [isToken, setIsToken] = useState<boolean>(false);

    return (
        <div className="App flex relative h-fit">
            <AlertsContext>
                {!isToken && <div className='fixed w-full top-2 z-2 justify-center'>
                    <AlertComponent isToken={isToken}/>
                </div>}
                <CheckIsLogin isToken={isToken} setIsToken={setIsToken}>
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
                                <Route path='/sample/manageExaminations/:sampleId/newExamination'
                                       element={<ExaminationForm/>}/>
                                <Route path='/sample/manageExaminations/:sampleId/newExamination/:examinationId'
                                       element={<ExaminationForm/>}/>
                                <Route path='/backup' element={<BackupView/>}/>
                                <Route path='/dictionary' element={<DictionariesView/>}/>
                                <Route path='/dictionary/clientDict' element={<ClientDict/>}/>
                                <Route path='/dictionary/indicationDict' element={<IndicationDict/>}/>
                                <Route path='/dictionary/codeDict' element={<CodeDict/>}/>
                                <Route path='/dictionary/inspectionDict' element={<InspectionDict/>}/>
                                <Route path='/dictionary/samplingStandardDict' element={<SamplingStandardDict/>}/>
                                <Route path='/dictionary/productGroupDict' element={<ProductGroupDict/>}/>
                                <Route path='/dictionary/assortmentDict' element={<AssortmentDict/>}/>
                                <Route path='/login' element={<LoginForm/>}/>
                                <Route path='/register' element={
                                    <PrivateRoute>
                                        <RegisterPage/>
                                    </PrivateRoute>
                                }/>
                                <Route path='/protocolReportData/:data' element={<ProtocolReportDataForm/>}/>
                                <Route path='/changePassword' element={<ChangePasswordForm/>}/>
                                <Route path='/sample/edit/:sampleId' element={<SampleForm/>}/>
                                <Route path='/importMethods' element={<ImportMethodsForm/>}/>
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
