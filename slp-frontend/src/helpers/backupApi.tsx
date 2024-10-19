import axios from "axios";
import {backendUrl, Header} from "../utils/urls";

const url = 'backup'


export const backup = (mode: string | undefined) => {
    if (mode !== undefined) {
        return axios.get(backendUrl + url + `/${mode}`, {
            ...Header(),
            responseType: 'blob'
        })
    }
    return null;
}


export const importBackup = (formData: FormData) => {
    return axios.post(backendUrl + url + `/import`, formData, {
        ...Header()
    });
};

