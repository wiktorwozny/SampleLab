import axios from "axios";
import { backendUrl, Header } from "../utils/urls";

const url = 'indication/'

const getIndicationsForSample = (sampleId: string | undefined) => {
    if (sampleId !== undefined) {
        return axios.get(backendUrl + url + `sample/${sampleId}`, Header())
    }
    return null;
}

const getIndicationById = (indicationId: string | undefined) => {
    if (indicationId !== undefined) {
        return axios.get(backendUrl + url + `${indicationId}`, Header());
    }
    return null;
}

export {
    getIndicationsForSample,
    getIndicationById
}
