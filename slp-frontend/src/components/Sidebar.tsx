import React, { useContext } from 'react';
import {NavigateFunction, useNavigate,} from "react-router-dom";
import { logout } from '../utils/logout';
import { AlertContext } from '../contexts/AlertsContext';

const Sidebar: React.FC = () => {

    const navigate: NavigateFunction = useNavigate();
    const {setAlertDetails} = useContext(AlertContext)

    return (
        <div className="sticky p-15 bg-gray-900 min-h-screen min-w-64 max-w-64">

            <div className="">
                <h2 className="pt-12 pb-4 text-white text-3xl">WIJHARS</h2>
                <ul className="list-none p-0">
                    <li className="my-1.5">
                        <a
                            className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/')}
                        >Lista próbek</a>
                    </li>
                    <li className="my-1.5">
                        <a
                            className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate("/addSample")}
                        >
                            Dodaj próbkę
                        </a>
                    </li>
                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/backup')}
                    >Archiwizacja danych
                    </a>
                    </li>

                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/dictionary')}
                    >Edycja danych
                    </a>
                    </li>

                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/importMethods')}
                    >Wczytaj metody
                    </a>
                    </li>

                    {localStorage.getItem('role') === 'ADMIN'&&<li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        onClick={() => navigate('/register')}
                    >Zajerestruj użytkownika
                    </a>
                    </li>}

                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
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