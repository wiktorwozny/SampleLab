import React from 'react';
import {useAppContext} from "../../contexts/AppContext";

const Main = ({children}: any): any => {

    const {isSidebarCollapsed} = useAppContext();

    return (
        <main
            className={`flex-grow transition-all duration-300 ${
                isSidebarCollapsed ? 'ml-16' : 'ml-64'
            }`}
        >{children}</main>
    )
}

export default Main;
