import React from 'react';
import {NavigateFunction, useNavigate,} from "react-router-dom";

const Sidebar: React.FC = () => {

    const navigate: NavigateFunction = useNavigate();


    return (
        <div className="sticky p-15 bg-gray-900 min-h-screen h-100% min-w-64 max-w-64">

            <div className="">
                <h2 className="pt-12 pb-4 text-white">Menu</h2>
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
                    >Słowniki
                    </a>
                    </li>
                </ul>
            </div>

        </div>
    );
};

export default Sidebar;