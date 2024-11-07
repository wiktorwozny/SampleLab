import axios from "axios"
import { backendUrl, Header } from "../utils/urls"

const url = "methods/";

export const importMethods = (file: File) => {
    const formData = new FormData();
    formData.append("file", file);
    
    return axios.post(backendUrl + url + "import", formData, Header());
}