import axios from "axios"
import { backendUrl } from "../utils/urls"
import { LoginData, RegisterData } from "../utils/types";

const url = "users/"

export const loginRequest = (data: LoginData) => {
    return axios.post(backendUrl + url + "login", data);
}

export const registerRequest = (data: RegisterData) => {
    return axios.post(backendUrl + url + "register", data)
}