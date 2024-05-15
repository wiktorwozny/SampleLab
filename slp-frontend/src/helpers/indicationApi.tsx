import axios from "axios";
import { backendUrl } from "../utils/urls";

const url = 'indication/'

const getIndicationsForSample = (sampleId: string | undefined) => {
    if (sampleId !== undefined) {
        return axios.get(backendUrl + url + `sample/${sampleId}`)
    }
    return null;
}

const getIndicationById = (indicationId: string | undefined) => {
    if (indicationId !== undefined) {
        return axios.get(backendUrl + url + `${indicationId}`);
    }
    return null;
}

export {
    getIndicationsForSample,
    getIndicationById
}
