import axios from "axios"
import { backendUrl } from "../utils/urls"

const url = 'data/'

export const getFiltersData = () => {
    return axios.get(backendUrl + url + "filters")
}