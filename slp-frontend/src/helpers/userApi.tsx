import axios from "axios"
import { backendUrl, Header } from "../utils/urls"
import { LoginData, RegisterData } from "../utils/types";

const url = "users/"

export const loginRequest = (data: LoginData) => {
    return axios.post(backendUrl + url + "login", data, Header());
}

export const registerRequest = (data: RegisterData) => {
    return axios.post(backendUrl + url + "register", data, Header())
}