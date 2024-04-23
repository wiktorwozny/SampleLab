import axios from "axios"
import { backendUrl } from "../utils/urls"
const url = "product-group/"

const getAllGroup = () =>{
    return axios.get(backendUrl+url+"list")
}

export {
    getAllGroup
}