import { createContext, useState } from "react";
import { AlertDetails } from "../utils/types";

interface AlertContextInterface {
    alertDetails: AlertDetails,
    setAlertDetails: React.Dispatch<React.SetStateAction<AlertDetails>>
}

const defaultAlertDetails: AlertDetails = { isAlert: false, message: "", type: "success" };

export const AlertContext = createContext<AlertContextInterface>({
    alertDetails:defaultAlertDetails,
    setAlertDetails: ()=>{}
});

const AlertsContext = ({children}:any) => {
    const [alertDetails, setAlertDetails] = useState<AlertDetails>(defaultAlertDetails); 
    return (<AlertContext.Provider value={{ alertDetails, setAlertDetails}}>
        {children}
    </AlertContext.Provider>)
}

export default AlertsContext;