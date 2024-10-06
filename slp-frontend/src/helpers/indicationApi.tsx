import axios from "axios";
import {backendUrl, Header} from "../utils/urls";
import {Indication} from "../utils/types";

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

const getAllIndications = () => {
    return axios.get(backendUrl + url + "list", Header());
}

const updateIndication = (item: Indication) => {
    return axios.put(backendUrl + url + 'update', item, Header());
}

const addIndication = (item: Indication) => {
    return axios.post(backendUrl + url + "save", item, Header());
}

const deleteIndication = (id: number | null) => {
    return axios.delete(backendUrl + url + `delete/${id}`, Header());
}

export {
    getIndicationsForSample,
    getIndicationById,
    getAllIndications,
    updateIndication,
    addIndication,
    deleteIndication
}
