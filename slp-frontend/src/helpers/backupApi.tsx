import axios from "axios";
import {backendUrl, Header} from "../utils/urls";

const url = 'backup'


export const backup = () => {

    return axios.get(backendUrl + url + `/`, {
        ...Header(),
        responseType: 'blob'
    })

}

