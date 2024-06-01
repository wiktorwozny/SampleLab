import React from 'react';
import {NavigateFunction, useNavigate} from "react-router-dom";

const Sidebar: React.FC = () => {

    const navigate: NavigateFunction = useNavigate();


    return (
        <div className="sidebar sticky p-15 bg-gray-900 h-screen top-0 w-64 min-w-1/12 ">
            <div className="m-2">
                <h2 className="pt-12 pb-4 text-white">Menu</h2>
                <ul className="list-none p-0">
                    <li className="my-1.5">
                        <a
                            className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                            onClick={() => navigate('/')}
                        >Lista próbek</a>
                    </li>
                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        href="#services">to be done</a></li>
                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        href="#about">to be done</a></li>
                    <li className="my-1.5"><a
                        className="text-white no-underline block p-2.5 rounded cursor-pointer hover:bg-gray-600"
                        href="#contact">to be done</a></li>
                </ul>
            </div>

        </div>
    );
};

export default Sidebar;