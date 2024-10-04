import axios from "axios"
import { backendUrl, Header } from "../utils/urls"

const url = "inspection/"

const getAllInspection = () => {
    return axios.get(backendUrl+url+"list", Header())
}

export {
    getAllInspection
}