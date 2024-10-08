import axios from "axios"
import {backendUrl, Header} from "../utils/urls"
import {Client} from "../utils/types";

const url = "client/"

const getAllClients = () => {
    return axios.get(backendUrl + url + "list", Header());
}

const updateClient = (item: Client) => {
    return axios.put(backendUrl + url + 'update', item, Header());
}

const addClient = (item: Client) => {
    return axios.post(backendUrl + url + "save", item, Header());
}

const deleteClient = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`, Header());
}

export {
    getAllClients,
    updateClient,
    addClient,
    deleteClient
}