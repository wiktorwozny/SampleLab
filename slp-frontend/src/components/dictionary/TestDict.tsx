import React from 'react';
import {HiAdjustmentsHorizontal} from "react-icons/hi2";
import Table from "../ui/Table";


interface User {
    id: number;
    name: string;
    age: number;
    email: string;
}

const users: User[] = [
    {id: 1, name: 'John Doe', age: 28, email: 'john@example.com'},
    {id: 2, name: 'Jane Smith', age: 34, email: 'jane@example.com'},
    {id: 3, name: 'Sam Johnson', age: 21, email: 'sam@example.com'},
];

const columns = [
    {header: 'ID', accessor: 'id'},
    {header: 'Name', accessor: 'name'},
    {header: 'Age', accessor: 'age'},
    {header: 'Email', accessor: 'email'},
];

const TestDict = () => {
    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Test dict</h1>
            <div className="w-full justify-end flex">
                <div
                    className="flex border relative mr-2 mb-2 p-2 border-black flex items-center hover:bg-gray-300 cursor-pointer"
                >
                    <div>Filtruj &nbsp;</div>
                    <HiAdjustmentsHorizontal className="text-3xl"></HiAdjustmentsHorizontal>
                </div>
            </div>
            <Table<User> columns={columns} data={users}/>
        </div>);
};

export default TestDict;
