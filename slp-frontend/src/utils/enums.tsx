import {ProgressState} from "./types";

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
