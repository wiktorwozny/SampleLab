import React, {useContext} from 'react';
import {NavigateFunction, useNavigate} from "react-router-dom";
import {logout} from '../utils/logout';
import {AlertContext} from '../contexts/AlertsContext';
import {backup} from "../helpers/backupApi";
import {checkResponse} from "../utils/checkResponse";
import {FaBars, FaTimes} from 'react-icons/fa';
import {useAppContext} from "../contexts/AppContext";

const Sidebar: React.FC<{}> = () => {
    const navigate: NavigateFunction = useNavigate();
    const {setAlertDetails} = useContext(AlertContext);

    const {isLoadingOverlayVisible, isSidebarCollapsed, toggleSidebar, toggleVisibility} = useAppContext();

    const formatDate = (date: Date) => {
        return date.toLocaleDateString('pl-PL', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };

    const handleBackup = async () => {
        if (isLoadingOverlayVisible) return;
        toggleVisibility();
        try {
            let response = await backup()
            setAlertDetails({isAlert: true, message: "Archiwizacja danych zakończona poprawnie", type: "success"})
            if (response != null) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const a = document.createElement('a');
                a.href = url;

                a.download = `backup_folder_with_csv_${formatDate(new Date())}.zip`;

                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
            checkResponse(err);
        } finally {
            toggleVisibility();
        }
    }

    return (
        <div
            className={`z-30 fixed top-0 left-0 h-full bg-gray-800 text-white transition-all duration-300 ${isSidebarCollapsed ? 'w-16' : 'w-64'}`}
        >
            <button
                className="absolute top-4 right-[-16px] bg-gray-800 text-white p-2 rounded-full shadow-md focus:outline-none hover:bg-gray-700"
                onClick={toggleSidebar}
            >
                {isSidebarCollapsed ? <FaBars size={20}/> : <FaTimes size={20}/>}
            </button>
            <div className={`overflow-hidden ${isSidebarCollapsed ? 'hidden' : 'block'} mt-12`}>

                <h2 className="text-xl font-bold mb-4 px-4">Menu</h2>
                <ul className="list-none text-left">
                    <li className="my-2">
                        <p
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/')}
                        >
                            Lista próbek
                        </p>
                    </li>
                    {localStorage.getItem('role') !== 'INTERN' && (
                        <li className="my-2">
                            <p
                                className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                                onClick={() => navigate("/addSample")}
                            >
                                Dodaj próbkę
                            </p>
                        </li>
                    )}
                    <li className="my-2">
                        <p
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/dictionary')}
                        >
                            Edytuj dane
                        </p>
                    </li>
                    {localStorage.getItem('role') !== 'INTERN' && (
                        <li className="my-2">
                            <p
                                className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                                onClick={() => navigate('/importMethods')}
                            >
                                Wczytaj metody
                            </p>
                        </li>
                    )}
                    <li className="my-2">
                        <p
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => handleBackup()}
                        >
                            Archiwizuj dane
                        </p>
                    </li>
                    {localStorage.getItem('role') === 'ADMIN' && (
                        <li className="my-2">
                            <p
                                className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                                onClick={() => navigate('/register')}
                            >
                                Zarejestruj użytkownika
                            </p>
                        </li>
                    )}
                    <li className="my-2">
                        <p
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/changePassword')}
                        >
                            Zmień hasło
                        </p>
                    </li>
                    {localStorage.getItem('role') === 'ADMIN' && (<li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/admin-panel')}
                        >
                            Lista użytkowników
                        </a>
                    </li>)}
                    <li className="my-2">
                        <p
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => logout(setAlertDetails)}
                        >
                            Wyloguj
                        </p>
                    </li>
                </ul>
            </div>
        </div>
    );
};

export default Sidebar;
