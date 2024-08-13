import React, {useEffect, useState} from 'react';
import {HiAdjustmentsHorizontal} from "react-icons/hi2";
import Table from "../ui/Table";
import {getAllClients} from "../../helpers/clientApi";
import {Address, Client} from "../../utils/types";


const formatAddress = (address: Address): string => {
    return `${address.street}, ${address.zipCode} ${address.city}`;
};

const columns = [
    {header: 'ID', accessor: 'id'},
    {header: 'Name', accessor: 'name'},
    {header: 'WIJHARS Code', accessor: 'wijharsCode'},

];

const TestDict = () => {
    const [clientsList, setClientsList] = useState<Client []>([])

    useEffect(() => {
        const getClients = async () => {
            try {
                let response = await getAllClients();
                console.log(response.data)
                if (response.status === 200) {
                    setClientsList(response.data)
                }
            } catch (err) {
                console.log(err)
            }
        }

        getClients();
    }, []);


    return (
        <div className="w-full">
            <h1 className="text-center font-bold text-3xl w-full my-3">Test dict</h1>
            <div className="w-full justify-end flex">
                <div
                    className="flex border relative mr-2 mb-2 p-2 border-black items-center hover:bg-gray-300 cursor-pointer"
                >
                    <div>Filtruj &nbsp;</div>
                    <HiAdjustmentsHorizontal className="text-3xl"></HiAdjustmentsHorizontal>
                </div>
            </div>
            <Table<Client> columns={columns} data={clientsList}/>
        </div>);
};

export default TestDict;
