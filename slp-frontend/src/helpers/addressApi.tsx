import axios from "axios"
import { backendUrl } from "../utils/urls"

const url = 'address/'

const getAllAddresses = () => {
    return axios.get(backendUrl + url + "list")
}

export {
    getAllAddresses
}