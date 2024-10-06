import axios from "axios"
import {backendUrl, Header} from "../utils/urls"
import {Inspection} from "../utils/types";

const url = "inspection/"

const getAllInspection = () => {
    return axios.get(backendUrl + url + "list", Header())
}

const updateInspection = (item: Inspection) => {
    return axios.put(backendUrl + url + 'update', item, Header());
}

const addInspection = (item: Inspection) => {
    return axios.post(backendUrl + url + "save", item, Header());
}

const deleteInspection = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`, Header());
}

export {
    getAllInspection,
    updateInspection,
    addInspection,
    deleteInspection
}