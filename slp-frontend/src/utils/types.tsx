import {AlertColor} from "@mui/material"
import {ProgressStateEnum, RoleEnum} from "./enums";
import {ReactNode} from "react";

export type Code = {
    id: string,
    name: string
}

export type Address = {
    id: number,
    street: string,
    zipCode: string,
    city: string
}

export type Client = {
    id: number | null,
    wijharsCode: string,
    name: string,
    address: Address
}

export type Inspection = {
    id: number,
    name: string
}

export type SamplingStandards = {
    id: number,
    name: string,
    groups: ProductGroup[]
}

export type Indication = {
    id: number,
    name: string,
    method: string,
    unit: string,
    laboratory: string,
    isOrganoleptic: boolean
}

export type Examination = {
    id: number;
    indication: Indication,
    sample: Sample,
    signage: string,
    nutritionalValue: string,
    specification: string,
    regulation: string,
    samplesNumber: number,
    result: string,
    startDate: Date,
    endDate: Date,
    methodStatus: string,
    uncertainty: number,
    lod: number,
    loq: number
}

export type ProductGroup = {
    id: number,
    name: string,
    samplingStandards?: SamplingStandards[],
    assortments?: Assortment[]
}

export type Assortment = {
    id: number,
    name: string,
    group: ProductGroup,
    indications: Indication[],
    organolepticMethod: string
}

export type ProductGroupSave = {
    id: number,
    name: string,
    samplingStandards?: number[]
}

export type AssortmentSave = {
    id: number,
    name: string,
    group: number,
    indications: number[],
    organolepticMethod: string
}

export type ReportData = {
    id: number,
    manufacturerName: string,
    manufacturerAddress: Address,
    manufacturerCountry: string,
    supplierName: string,
    supplierAddress: Address,
    sellerName: string,
    sellerAddress: Address,
    recipientName: string,
    recipientAddress: Address,
    productionDate: Date,
    batchNumber: number,
    batchSizeProd: string,
    batchSizeStorehouse: string,
    sampleCollector: string,
    samplePacking: string,
    sampleCollectionSite: string,
    jobNumber: number,
    mechanism: string,
    deliveryMethod: string,
    collectionDate: Date,
    protocolNumber: string,
    sampleId: number
}

export type Sample = {
    id: number,
    code: Code,
    client: Client,
    assortment: Assortment,
    admissionDate: Date,
    expirationDate: Date,
    expirationComment: string,
    examinationExpectedEndDate: Date,
    size: string,
    state: string,
    analysis: boolean,
    inspection: Inspection,
    samplingStandard: SamplingStandards,
    reportData: ReportData
    progressStatus: ProgressStateEnum
}

export type FilterRequest = {
    fieldName: string,
    ascending: boolean,
    pageNumber: number,
    pageSize: number,
    filters: FiltersData
    fuzzySearch: string;
}

export type ProgressState = {
    value: ProgressStateEnum,
    label: string
}

export type Role = {
    value: RoleEnum,
    label: string
}

export type SummarySample = {
    id: number,
    code: string,
    group: string,
    assortment: string,
    clientName: string,
    admissionDate: Date
    progressStatus: ProgressStateEnum,
}

export type FilterResponse = {
    totalPages: number,
    samples: SummarySample[]
}

export type FiltersData = {
    codes: string[],
    clients: string[],
    groups: string[]
    progressStatuses: string[]
}

export type AlertDetails = {
    isAlert: boolean,
    message: string,
    type: AlertColor
}

export type LoginData = {
    email: string,
    password: string
}

export type RegisterData = {
    name: string,
    email: string,
    role: RoleEnum
}

export interface Column<T> {
    header: string;
    accessor: keyof T | string;
    render?: (value: T[keyof T]) => ReactNode;
}

export type ChangePasswordPayload = {
    oldPassword: String, 
    newPassword: String
}

export type User = {
    email: String,
    name: String,
    role: RoleEnum
}