import React, {useContext, useState} from 'react';
import {NavigateFunction, useNavigate} from "react-router-dom";
import {logout} from '../utils/logout';
import {AlertContext} from '../contexts/AlertsContext';
import {backup} from "../helpers/backupApi";
import {checkResponse} from "../utils/checkResponse";
import LoadingOverlay from "./ui/LoadingOverlay";
import {FaBars, FaTimes} from 'react-icons/fa'; // Import ikon

const Sidebar: React.FC<{ isCollapsed: boolean; toggleSidebar: () => void }> = ({isCollapsed, toggleSidebar}) => {
    const navigate: NavigateFunction = useNavigate();
    const {setAlertDetails} = useContext(AlertContext);
    const [isLoading, setIsLoading] = useState(false);

    const formatDate = (date: Date) => {
        return date.toLocaleDateString('pl-PL', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };

    const handleBackup = async (mode: string) => {
        if (isLoading) return;
        setIsLoading(true);
        try {
            let response = await backup(mode)
            setAlertDetails({isAlert: true, message: "Archiwizacja danych zakończona poprawnie", type: "success"})
            if (response != null) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const a = document.createElement('a');
                a.href = url;
                if (mode === "CSV") {
                    a.download = `backup_folder_with_csv_${formatDate(new Date())}.zip`;
                } else {
                    a.download = `backup_${formatDate(new Date())}_.sql`;
                }
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
            setIsLoading(false);
        }
    }

    return (
        <div
            className={`fixed top-0 left-0 h-full bg-gray-800 text-white transition-all duration-300 ${isCollapsed ? 'w-16' : 'w-64'}`}
        >
            {/* Ikona do zwijania/rozwijania */}
            <button
                className="absolute top-4 right-[-16px] bg-gray-800 text-white p-2 rounded-full shadow-md focus:outline-none hover:bg-gray-700"
                onClick={toggleSidebar}
            >
                {isCollapsed ? <FaBars size={20}/> : <FaTimes size={20}/>}
            </button>

            {/* Treść Sidebaru */}
            <div className={`overflow-hidden ${isCollapsed ? 'hidden' : 'block'} mt-12`}>
                {isLoading && <LoadingOverlay message="Proszę czekać, trwa archiwizacja danych..."/>}
                <h2 className="text-xl font-bold mb-4 px-4">Menu</h2>
                <ul className="list-none text-left">
                    <li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/')}
                        >
                            Lista próbek
                        </a>
                    </li>
                    <li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"

                            onClick={() => navigate("/addSample")}
                        >
                            Dodaj próbkę
                        </a>
                    </li>
                    <li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"

                            onClick={() => navigate('/dictionary')}
                        >
                            Edycja danych
                        </a>
                    </li>
                    <li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"

                            onClick={() => navigate('/importMethods')}
                        >
                            Wczytaj metody
                        </a>
                    </li>
                    <li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"

                            onClick={() => handleBackup('CSV')}
                        >
                            Archiwizuj dane
                        </a>
                    </li>
                    {localStorage.getItem('role') === 'ADMIN' && (
                        <li className="my-2">
                            <a
                                className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"

                                onClick={() => navigate('/register')}
                            >
                                Zarejestruj użytkownika
                            </a>
                        </li>
                    )}
                    <li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"

                            onClick={() => navigate('/changePassword')}
                        >
                            Zmień hasło
                        </a>
                    </li>
                    <li className="my-2">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"

                            onClick={() => logout(setAlertDetails)}
                        >
                            Wyloguj
                        </a>
                    </li>
                </ul>
            </div>
        </div>
    );
};

export default Sidebar;
