import axios from "axios"
import { backendUrl } from "../utils/urls"

const url = "inspection/"

const getAllInspection = () => {
    return axios.get(backendUrl+url+"list")
}

export {
    getAllInspection
}