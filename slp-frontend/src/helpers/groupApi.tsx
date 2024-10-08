import axios from "axios"
import {backendUrl, Header} from "../utils/urls"
import {ProductGroupSave} from "../utils/types";

const url = "product-group/"

const getAllGroup = () => {
    return axios.get(backendUrl + url + "list", Header())
}
const updateGroup = (item: ProductGroupSave) => {
    return axios.put(backendUrl + url + 'update', item, Header());
}

const addGroup = (item: ProductGroupSave) => {
    return axios.post(backendUrl + url + 'save', item, Header());
}

const deleteGroup = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`, Header());
}

export {
    getAllGroup,
    updateGroup,
    addGroup,
    deleteGroup
}