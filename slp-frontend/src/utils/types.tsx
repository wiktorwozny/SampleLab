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
    id: number,
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
    groups: ProductGroup []
}
export type Indication = {
    id: number,
    name: string,
    norm: string,
    unit: string,
    laboratory: string,
    // groups: ProductGroup []
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
    indications?: Indication [],
    samplingStandards?: SamplingStandards []
}

export type ReportData = {
    id: number,
    manufacturerName: string,
    manufacturerAddress: Address,
    supplierName: string,
    supplierAddress: Address,
    sellerName: string,
    sellerAddress: Address,
    recipientName: string,
    recipientAddress: Address,
    jobNumber: number,
    mechanism: string,
    deliveryMethod: string,
    sampleId: number
}

export type Sample = {
    id: number,
    code: Code,
    client: Client,
    assortment: string,
    admissionDate: Date,
    expirationDate: Date,
    expirationComment: string,
    examinationEndDate: Date,
    size: string,
    state: string,
    analysis: boolean,
    inspection: Inspection,
    group: ProductGroup,
    samplingStandard: SamplingStandards,
    reportData: ReportData
}

export type FilterRequest = {
    fieldName: string,
    ascending: boolean,
    pageNumber: number,
    pageSize: number,
    filters: FiltersData
}

export type FilterResponse = {
    id: number,
    code: string,
    group: string,
    assortment: string,
    clientName: string,
    admissionDate: Date
}

export type FiltersData = {
    codes: string[],
    clients: string[],
    groups: string[]
}