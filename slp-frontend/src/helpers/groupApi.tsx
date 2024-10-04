import axios from "axios"
import { backendUrl, Header } from "../utils/urls"
const url = "product-group/"

const getAllGroup = () =>{
    return axios.get(backendUrl+url+"list", Header())
}

export {
    getAllGroup
}