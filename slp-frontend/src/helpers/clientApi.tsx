import axios from "axios"
import { backendUrl } from "../utils/urls"

const url = "client/"

const getAllClients = () => {
    return axios.get(backendUrl+url+"list");
}

export {
    getAllClients
}