import axios from "axios"
import { backendUrl, Header } from "../utils/urls"

const url = "client/"

const getAllClients = () => {
    return axios.get(backendUrl+url+"list", Header());
}

export {
    getAllClients
}