import axios from "axios";
import {backendUrl} from "../utils/urls";

const url = 'backup'


export const backup = (mode: string | undefined) => {
    if (mode !== undefined) {
        return axios.get(backendUrl + url + `/${mode}`)
    }
    return null;
}

