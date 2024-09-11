import { ProgressState } from "./types";
import { Role } from "./types";
export enum ProgressStateEnum {
    TODO = 'TODO',
    IN_PROGRESS = 'IN_PROGRESS',
    DONE = 'DONE'
}

export const ProgressStateEnumDesc: ProgressState[] = [
    {value: ProgressStateEnum.TODO, label: 'To do'},
    {value: ProgressStateEnum.IN_PROGRESS, label: 'In progress'},
    {value: ProgressStateEnum.DONE, label: 'Done'}
];

export enum RoleEnum {
    WORKER = "WORKER",
    ADMIN = "ADMIN"
}

export const RoleEnumDesc: Role[] = [
    {value: RoleEnum.WORKER, label: "Pracownik"},
    {value: RoleEnum.ADMIN, label: "Admin"}
]