import axios from "axios"
import {backendUrl, Header} from "../utils/urls"
import {Code} from "../utils/types";

const url = 'code/'

const getAllCodes = () => {
    return axios.get(backendUrl + url + "list", Header())
}
const updateCode = (item: Code) => {
    return axios.put(backendUrl + url + 'update', item, Header());
}

const addCode = (item: Code) => {
    return axios.post(backendUrl + url + "save", item, Header());
}

const deleteCode = (id: string | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`, Header());
}


export {
    getAllCodes,
    updateCode,
    addCode,
    deleteCode
}