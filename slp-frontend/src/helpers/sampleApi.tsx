import axios from "axios"
import { backendUrl } from "../utils/urls"

const url = `sample/`

const getAllSamples = () => {
    return axios.get(backendUrl + url + `list`)
}

export {
    getAllSamples
}