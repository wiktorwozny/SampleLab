import axios from "axios";
import {backendUrl} from "../utils/urls";

const url = 'backup/'

export const backup = async () => {
    return axios.get(backendUrl + url);

};