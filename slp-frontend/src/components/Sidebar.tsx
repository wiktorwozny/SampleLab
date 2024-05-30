import React from 'react';
import {NavigateFunction, useNavigate} from "react-router-dom";

const Sidebar: React.FC = () => {

    const navigate: NavigateFunction = useNavigate();


    return (
        <div className="sidebar sticky">
            <h2>Menu</h2>
            <ul>
                <li><a onClick={() => navigate('/')}>Home</a></li>
                <li><a href="#services">Services</a></li>
                <li><a href="#about">About</a></li>
                <li><a href="#contact">Contact</a></li>
            </ul>
        </div>
    );
};

export default Sidebar;