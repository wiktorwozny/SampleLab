import React, {useContext} from 'react';
import {NavigateFunction, useNavigate,} from "react-router-dom";
import {AlertContext} from "../contexts/AlertsContext";
import {backup} from "../helpers/backupApi";

const Sidebar: React.FC = () => {
    const {setAlertDetails} = useContext(AlertContext);

    const navigate: NavigateFunction = useNavigate();
    const handleBackup = async () => {
        try {
            let response = await backup()
            console.log(response)
            setAlertDetails({isAlert: true, message: "Archiwizacja danych zakończona poprawnie", type: "success"})

        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})

        }
    }

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
                        onClick={() => handleBackup()}>back-up danych</a></li>
                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        href="#contact">to be done</a></li>
                </ul>
            </div>

        </div>
    );
};

export default Sidebar;