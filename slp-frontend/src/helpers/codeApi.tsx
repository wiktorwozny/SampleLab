import axios from "axios"
import { backendUrl } from "../utils/urls"
const url='code/'

const getAllCodes = () => {
    return axios.get(backendUrl+url+"list")
}


export {
    getAllCodes
}