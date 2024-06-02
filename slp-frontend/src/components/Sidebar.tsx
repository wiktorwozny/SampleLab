import React from 'react';
import {NavigateFunction, useNavigate,} from "react-router-dom";
import NavigationArrows from "./ui/NavigationArrows";

const Sidebar: React.FC = () => {

    const navigate: NavigateFunction = useNavigate();


    const handlePrevious = () => {
        navigate(-1);
    };

    const handleNext = () => {
        console.log('Next clicked');
    };

    return (
        <div className="sticky p-15 bg-gray-900 h-screen min-w-64 max-w-64">
            <NavigationArrows onPrevious={handlePrevious} onNext={handleNext}/>
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