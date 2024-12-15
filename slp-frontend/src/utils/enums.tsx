import {ProgressState, Role} from "./types";

export enum ProgressStateEnum {
    IN_PROGRESS = 'IN_PROGRESS',
    DONE = 'DONE'
}

export const ProgressStateEnumDesc: ProgressState[] = [
    {value: ProgressStateEnum.IN_PROGRESS, label: 'W trakcie'},
    {value: ProgressStateEnum.DONE, label: 'Gotowe'}
];

export const ProgressStateMap = new Map<ProgressStateEnum, string>([
    [ProgressStateEnum.IN_PROGRESS, 'W trakcie'],
    [ProgressStateEnum.DONE, 'Gotowe'],
]);


export enum RoleEnum {
    ADMIN = "ADMIN",
    WORKER = "WORKER",
    INTERN = "INTERN"
}

export const RoleEnumDesc: Role[] = [
    {value: RoleEnum.ADMIN, label: "Administrator"},
    {value: RoleEnum.WORKER, label: "Pracownik"},
    {value: RoleEnum.INTERN, label: "Praktykant"}
]