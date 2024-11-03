import axios from "axios"
import { backendUrl, Header } from "../utils/urls"

const url = "methods/";

export const importMethods = (data: any) => {
    const formData = new FormData();
    formData.append("file", data.file[0]);
    
    return axios.post(backendUrl + url + "import", formData, Header());
}