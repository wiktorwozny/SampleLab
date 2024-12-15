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

import {AdminRoute, WorkerRoute} from './components/PrivateRoute';
import CheckIsLogin from './components/CheckIsLogin';
import ProtocolReportDataForm from "./components/ProtocolReportDataForm";
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
import DictionariesPage from "./pages/DictionariesPage";
import Footer from "./components/Footer";
import Header from "./components/Header";
import AdminPage from './pages/AdminPage';
import {AppProvider} from "./contexts/AppContext";
import LoadingOverlay from "./components/ui/LoadingOverlay";
import Main from './components/ui/Main';

function App() {
    const [isToken, setIsToken] = useState<boolean>(false);

    return (
        <AppProvider>
            <div className="App flex flex-col min-h-screen relative">
                <LoadingOverlay message={'Trwa pobieranie...'}/>
                <AlertsContext>
                    {!isToken && (
                        <div className="fixed w-full top-2 right-2 z-30">
                            <AlertComponent isToken={isToken}/>
                        </div>
                    )}
                    <div className="fixed w-full top-2 z-30">
                        <AlertComponent/>
                    </div>
                    <CheckIsLogin isToken={isToken} setIsToken={setIsToken}>
                        <BrowserRouter>
                            <Header/>
                            <Sidebar/>
                            <Main>
                                <div>
                                    <Routes>
                                        <Route path="/" element={<SampleListPage/>}/>
                                        <Route path="/addSample" element={<WorkerRoute><SampleForm/></WorkerRoute>}/>
                                        <Route path="/sample/:sampleId" element={<SingleSamplePage/>}/>
                                        <Route path="/sample/addReportData/:sampleId" element={<WorkerRoute><ReportDataForm/></WorkerRoute>}/>
                                        <Route
                                            path="/sample/manageExaminations/:sampleId"
                                            element={<ExaminationsList/>}
                                        />
                                        <Route
                                            path="/sample/manageExaminations/:sampleId/newExamination"
                                            element={<ExaminationForm/>}
                                        />
                                        <Route
                                            path="/sample/manageExaminations/:sampleId/newExamination/:examinationId"
                                            element={<ExaminationForm/>}
                                        />
                                        <Route path="/dictionary" element={<DictionariesPage/>}/>
                                        <Route path="/dictionary/clientDict" element={<ClientDict/>}/>
                                        <Route path="/dictionary/indicationDict" element={<IndicationDict/>}/>
                                        <Route path="/dictionary/codeDict" element={<CodeDict/>}/>
                                        <Route path="/dictionary/inspectionDict" element={<InspectionDict/>}/>
                                        <Route
                                            path="/dictionary/samplingStandardDict"
                                            element={<SamplingStandardDict/>}
                                        />
                                        <Route path="/dictionary/productGroupDict" element={<ProductGroupDict/>}/>
                                        <Route path="/dictionary/assortmentDict" element={<AssortmentDict/>}/>
                                        <Route path="/login" element={<LoginForm/>}/>
                                        <Route
                                            path="/register"
                                            element={
                                                <AdminRoute>
                                                    <RegisterPage/>
                                                </AdminRoute>
                                            }
                                        />
                                        <Route
                                            path="/protocolReportData/:data"
                                            element={<WorkerRoute><ProtocolReportDataForm/></WorkerRoute>}
                                        />
                                        <Route path="/changePassword" element={<ChangePasswordForm/>}/>
                                        <Route path="/sample/edit/:sampleId" element={<WorkerRoute><SampleForm/></WorkerRoute>}/>
                                        <Route path="/importMethods" element={<WorkerRoute><ImportMethodsForm/></WorkerRoute>}/>
                                        <Route
                                            path="/admin-panel"
                                            element={
                                                <AdminRoute>
                                                    <AdminPage/>
                                                </AdminRoute>
                                            }
                                        />
                                    </Routes>
                                </div>
                            </Main>
                            <Footer/>
                        </BrowserRouter>
                    </CheckIsLogin>
                </AlertsContext>
            </div>
        </AppProvider>
    );
}

export default App;

