import axios from "axios"
import { backendUrl } from "../utils/urls"
import { Header } from "../utils/urls"
import {Code} from "../utils/types";
const url='code/'

const getAllCodes = () => {
    return axios.get(backendUrl+url+"list", Header())
}
const updateCode = (item: Code) => {
    return axios.put(backendUrl + url + 'update', item);
}

const addCode = (item: Code) => {
    return axios.post(backendUrl + url + "save", item);
}

const deleteCode = (id: string | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`);
}


export {
    getAllCodes,
    updateCode,
    addCode,
    deleteCode
}