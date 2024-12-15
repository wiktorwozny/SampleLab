import {Dispatch, SetStateAction} from "react";
import {AlertDetails} from "./types";

export const handleApiError = (
    error: any,
    handleClose: () => void,
    setAlertDetails: Dispatch<SetStateAction<AlertDetails>>,
    defaultMessage: string = "Wystąpił błąd, spróbuj ponownie później."
) => {
    let message: string = "Błędna walidacja pól: \n";
    if (error.response && error.response.data && error.response.data.errors) {
        error.response.data.errors.forEach((err: string) => {
            message += " - " + err + "\n";
        });
    } else {
        message += defaultMessage;
    }
    handleClose();
    console.log(message);
    setAlertDetails({
        isAlert: true,
        message: message.trim(),
        type: "error",
    });
};
