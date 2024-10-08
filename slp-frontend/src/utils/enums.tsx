import { ProgressState } from "./types";
import { Role } from "./types";
export enum ProgressStateEnum {
    TODO = 'Do zrobienia',
    IN_PROGRESS = 'W trakcie',
    DONE = 'Gotowe'
}

export const ProgressStateEnumDesc: ProgressState[] = [
    {value: ProgressStateEnum.TODO, label: 'Do zrobienia'},
    {value: ProgressStateEnum.IN_PROGRESS, label: 'W trakcie'},
    {value: ProgressStateEnum.DONE, label: 'Gotowe'}
];

export enum RoleEnum {
    WORKER = "WORKER",
    ADMIN = "ADMIN"
}

export const RoleEnumDesc: Role[] = [
    {value: RoleEnum.WORKER, label: "Pracownik"},
    {value: RoleEnum.ADMIN, label: "Admin"}
]