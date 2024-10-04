import axios from "axios"
import { backendUrl, Header } from "../utils/urls"

const url = 'data/'

export const getFiltersData = () => {
    return axios.get(backendUrl + url + "filters", Header())
}