import axios from "axios"
import {backendUrl} from "../utils/urls"
import {ProductGroupSave} from "../utils/types";

const url = "product-group/"

const getAllGroup = () => {
    return axios.get(backendUrl + url + "list")
}
const updateGroup = (item: ProductGroupSave) => {
    return axios.put(backendUrl + url + 'update', item);
}

const addGroup = (item: ProductGroupSave) => {
    return axios.post(backendUrl + url + 'save', item);
}

const deleteGroup = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`);
}

export {
    getAllGroup,
    updateGroup,
    addGroup,
    deleteGroup
}