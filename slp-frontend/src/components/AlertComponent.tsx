import { Collapse, Alert, IconButton, AlertTitle } from "@mui/material";
import { IoClose } from "react-icons/io5";
import { AlertContext } from "../contexts/AlertsContext";
import { useContext, useEffect } from "react";
import { useRef, RefObject } from "react";
const AlertComponent = () => {
    const {alertDetails, setAlertDetails} = useContext(AlertContext);
    const alertRef: RefObject<HTMLDivElement> = useRef<HTMLDivElement>(null);
    useEffect(()=>{
        if(alertDetails.isAlert){
            setTimeout(()=>{
                setAlertDetails(prev=>({...prev,isAlert:false}))
            },10000)
            console.log(alertRef.current)
            let width = alertRef.current?.clientWidth
            if (width && alertRef.current) {
                alertRef.current.style.right = `${width / 2}px`;
            }
        }
    },[alertDetails])
    return (<Collapse in = {alertDetails.isAlert} className="w-full flex justify-center">
        <Alert
            className="w-fit relative mx-auto"
            ref={alertRef}
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