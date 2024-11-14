import React from 'react';
import {BeakerIcon} from "@heroicons/react/16/solid";

const Header: React.FC = () => {
    return (
        <header className="z-20 fixed top-0 right-0 left-0 bg-gray-800 text-white shadow-lg">
            <div className="container mx-auto flex items-center justify-center p-6">
                <BeakerIcon className="h-8 w-8 mr-3 text-white"/>
                <h1 className="text-3xl font-extrabold font-poppins tracking-wide">
                    SampleLab
                </h1>
            </div>
        </header>
    );
};

export default Header;