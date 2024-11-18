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

function App() {
    const [isToken, setIsToken] = useState<boolean>(false);
    const [isSidebarCollapsed, setIsSidebarCollapsed] = useState<boolean>(false);

    const toggleSidebar = () => {
        setIsSidebarCollapsed(!isSidebarCollapsed);
    };

    return (
        <div className="App flex flex-col min-h-screen relative w-full">
            <AlertsContext>
                {!isToken && (
                    <div className="fixed w-full top-2 z-2 justify-center">
                        <AlertComponent isToken={isToken}/>
                    </div>
                )}
                <CheckIsLogin isToken={isToken} setIsToken={setIsToken}>
                    <BrowserRouter>
                        <Header/>
                        <Sidebar isCollapsed={isSidebarCollapsed} 
                                 toggleSidebar={toggleSidebar}/>{/* Sidebar jako element `fixed` */}
                        <main
                            className={`flex-grow transition-all duration-300 ${
                                isSidebarCollapsed ? 'ml-16' : 'ml-64'
                            }`}
                        >
                            <div> {/* Margines dla treści głównej */}
                                <div className="fixed w-full top-2 z-2 justify-center">
                                    <AlertComponent/>
                                </div>
                                <Routes>
                                    <Route path="/" element={<SampleListPage/>}/>
                                    <Route path="/addSample" element={<SampleForm/>}/>
                                    <Route path="/sample/:sampleId" element={<SingleSamplePage/>}/>
                                    <Route path="/sample/addReportData/:sampleId" element={<ReportDataForm/>}/>
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
                                    <Route path="/backup" element={<BackupView/>}/>
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
                                            <PrivateRoute>
                                                <RegisterPage/>
                                            </PrivateRoute>
                                        }
                                    />
                                    <Route
                                        path="/protocolReportData/:data"
                                        element={<ProtocolReportDataForm/>}
                                    />
                                    <Route path="/changePassword" element={<ChangePasswordForm/>}/>
                                    <Route path="/sample/edit/:sampleId" element={<SampleForm/>}/>
                                    <Route path="/importMethods" element={<ImportMethodsForm/>}/>
                                    <Route
                                        path="/admin-panel"
                                        element={
                                            <PrivateRoute>
                                                <AdminPage/>
                                            </PrivateRoute>
                                        }
                                    />
                                </Routes>
                            </div>
                        </main>
                        <Footer/>
                    </BrowserRouter>
                </CheckIsLogin>
            </AlertsContext>
        </div>
    );
}

export default App;

