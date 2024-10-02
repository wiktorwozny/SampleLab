import axios from "axios"
import {backendUrl} from "../utils/urls"
import {Inspection} from "../utils/types";

const url = "inspection/"

const getAllInspection = () => {
    return axios.get(backendUrl + url + "list")
}

const updateInspection = (item: Inspection) => {
    return axios.put(backendUrl + url + 'update', item);
}

const addInspection = (item: Inspection) => {
    return axios.post(backendUrl + url + "save", item);
}

const deleteInspection = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`);
}

export {
    getAllInspection,
    updateInspection,
    addInspection,
    deleteInspection
}