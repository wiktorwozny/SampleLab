import axios from "axios"
import { backendUrl } from "../utils/urls"
import { Header } from "../utils/urls"
const url='code/'

const getAllCodes = () => {
    return axios.get(backendUrl+url+"list", Header())
}


export {
    getAllCodes
}