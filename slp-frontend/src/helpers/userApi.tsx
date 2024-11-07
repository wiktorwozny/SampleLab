import axios from "axios"
import { backendUrl, Header } from "../utils/urls"
import { LoginData, RegisterData, ChangePasswordPayload } from "../utils/types";

const url = "users/"

export const loginRequest = (data: LoginData) => {
    return axios.post(backendUrl + url + "login", data, Header());
}

export const registerRequest = (data: RegisterData) => {
    return axios.post(backendUrl + url + "register", data, Header())
}

export const changePassword = (data: ChangePasswordPayload) => {
    return axios.post(backendUrl + url + "change-password", data, Header())
}