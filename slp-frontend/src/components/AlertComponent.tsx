import { Collapse, Alert, IconButton, AlertTitle } from "@mui/material";
import { IoClose } from "react-icons/io5";
import { AlertContext } from "../contexts/AlertsContext";
import { useContext, useEffect } from "react";
const AlertComponent = () => {
    const {alertDetails, setAlertDetails} = useContext(AlertContext);
    useEffect(()=>{
        if(alertDetails.isAlert){
            setTimeout(()=>{
                setAlertDetails(prev=>({...prev,isAlert:false}))
            },10000)
        }
    },[alertDetails])
    return (<Collapse in = {alertDetails.isAlert}>
        <Alert
            severity={alertDetails.type}
            action={
                <IconButton
                aria-label="close"
                color="inherit"
                size="small"
                onClick={() => {
                    setAlertDetails(prev=>({...prev,isAlert:false}))
                }}
                >
                    <IoClose fontSize="inherit" />
                </IconButton>
            }
        >
            <AlertTitle>{alertDetails.type==="success"?"Sukces":"Błąd"}</AlertTitle>
            {alertDetails.message}
        </Alert>
    </Collapse>)
}

export default AlertComponent;