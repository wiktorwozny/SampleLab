import React, {createContext, ReactNode, useContext, useState} from 'react';


interface AppContextType {
    isLoadingOverlayVisible: boolean;
    isSidebarCollapsed: boolean;
    toggleVisibility: () => void;
    toggleSidebar: () => void;
}

const AppContext = createContext<AppContextType | undefined>(undefined);

interface AppProviderProps {
    children: ReactNode;
}

export const AppProvider: React.FC<AppProviderProps> = ({children}) => {
    const [isLoadingOverlayVisible, setIsLoadingOverlayVisible] = useState<boolean>(false);
    const [isSidebarCollapsed, setIsSidebarCollapsed] = useState<boolean>(false);

    const toggleVisibility = () => {
        setIsLoadingOverlayVisible(prev => !prev);
    }

    const toggleSidebar = () => {
        setIsSidebarCollapsed(!isSidebarCollapsed);
    };


    return (
        <AppContext.Provider value={{isLoadingOverlayVisible, isSidebarCollapsed, toggleVisibility, toggleSidebar}}>
            {children}
        </AppContext.Provider>
    );
};

export const useAppContext = (): AppContextType => {
    const context = useContext(AppContext);
    if (!context) {
        throw new Error('useAppContext must be used within an AppProvider');
    }
    return context;
};
