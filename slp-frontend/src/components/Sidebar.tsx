import React, {useContext, useState} from 'react';
import {NavigateFunction, useNavigate,} from "react-router-dom";
import {logout} from '../utils/logout';
import {AlertContext} from '../contexts/AlertsContext';
import {backup} from "../helpers/backupApi";
import {checkResponse} from "../utils/checkResponse";
import LoadingOverlay from "./ui/LoadingOverlay";

const Sidebar: React.FC = () => {

    const navigate: NavigateFunction = useNavigate();
    const {setAlertDetails} = useContext(AlertContext)
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
        <div className="fixed top-0 left-0 p-15 bg-gray-800 min-h-screen min-w-64 max-w-64">
            {isLoading && <LoadingOverlay message="Proszę czekać, trwa archiwizacja danych..."/>}
            <div className="">
                <h2 className="pt-12 pb-4 text-white text-xl">Menu</h2>
                <ul className="list-none p-0 text-left">
                    <li className="my-1.5">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/')}
                        >Lista próbek</a>
                    </li>
                    <li className="my-1.5">
                        <a
                            className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate("/addSample")}
                        >
                            Dodaj próbkę
                        </a>
                    </li>
                    <li className="my-1.5"><a
                        className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/dictionary')}
                    >Edycja danych
                    </a>
                    </li>

                    <li className="my-1.5"><a
                        className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/importMethods')}
                    >Wczytaj metody
                    </a>
                    </li>

                    <li className="my-1.5"><a
                        className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => handleBackup('CSV')}
                    >Archiwizuj dane
                    </a>
                    </li>

                    {localStorage.getItem('role') === 'ADMIN' && <li className="my-1.5"><a
                        className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/register')}
                    >Zajerestruj użytkownika
                    </a>
                    </li>}

                    <li className="my-1.5"><a
                        className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/changePassword')}
                    >Zmień hasło
                    </a>
                    </li>

                    <li className="my-1.5"><a
                        className="pl-10 text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => logout(setAlertDetails)}
                    >Wyloguj
                    </a>
                    </li>
                </ul>
            </div>

        </div>
    );
};

export default Sidebar;