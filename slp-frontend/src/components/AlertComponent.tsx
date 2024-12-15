import {Alert, AlertTitle, Collapse, IconButton} from "@mui/material";
import {IoClose} from "react-icons/io5";
import {AlertContext} from "../contexts/AlertsContext";
import {RefObject, useContext, useEffect, useRef} from "react";

const AlertComponent = ({isToken = true}: any) => {
    const {alertDetails, setAlertDetails} = useContext(AlertContext);
    const alertRef: RefObject<HTMLDivElement> = useRef<HTMLDivElement>(null);
    useEffect(() => {
        if (alertDetails.isAlert) {
            setTimeout(() => {
                setAlertDetails(prev => ({...prev, isAlert: false}))
            }, 10000)
            console.log(alertRef.current)
            let width = alertRef.current?.clientWidth
            if (width && alertRef.current) {
                alertRef.current.style.right = `0`;
            }
        }
    }, [alertDetails])
    return (<Collapse in={alertDetails.isAlert} className="w-full flex justify-center ">
        <Alert
            className={`w-fit ${isToken ? "relative" : ""} mx-auto right-0`}
            ref={alertRef}
            severity={alertDetails.type}
            action={
                <IconButton
                    aria-label="close"
                    color="inherit"
                    size="small"
                    onClick={() => {
                        setAlertDetails(prev => ({...prev, isAlert: false}))
                    }}
                >
                    <IoClose fontSize="inherit"/>
                </IconButton>
            }
        >
            <AlertTitle>{alertDetails.type === "success" ? "Sukces" : "Błąd"}</AlertTitle>
            <div className='text-left'>
                {alertDetails.message.split('\n').map((line, index) => (
                    <span key={index}>
                    {line}
                        <br/>
                </span>
                ))}
            </div>

        </Alert>
    </Collapse>)
}

export default AlertComponent;