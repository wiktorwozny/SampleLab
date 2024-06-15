import { IoClose } from "react-icons/io5";
import Chip from '@mui/material/Chip';
import { ChipFilter } from "./ui/ChipFilter";
import { useEffect, useState } from "react";
import { getFiltersData } from "../helpers/dataApi";
const FilterComponet = ({setIsFilters, isFilters, filtersData, selectedFilters, setSelectedFilters}:any) => {
    return(<div className="fixed w-full h-full top-0 flex justify-end left-0">
        <div className={`absolute bg-gray-500 opacity-50 w-full h-full z-0`} onClick={()=>{setIsFilters(false)}}/>
        <div className={`${isFilters? "left-0":"left-1/3"} w-1/3 z-10 bg-white relative flex flex-col duration-75 font-bold p-3`}>
            <div className="w-full justify-between flex">
                <div className="text-2xl">Filtruj</div>
                <div className="text-3xl" onClick={()=>{setIsFilters(false)}}>
                    <IoClose className="text-3xl cursor-pointer"/>
                </div>
            </div>

            <div className="w-full border my-2"></div>
            <div className="flex flex-col">
                <h2 className="text-start text-xl">Kod próbki</h2>
                <div className="flex mt-2 flex-wrap">
                    {filtersData&&filtersData.codes.map((el:string,i:number)=>(<ChipFilter 
                        label={`${el}`} 
                        key={i}
                        keyValue="codes"
                        setSelectedFilters={setSelectedFilters}
                        selectedFilters={selectedFilters}
                    />))}
                </div>
            </div>

            <div className="w-full border my-2"></div>
            <div className="flex flex-col">
                <h2 className="text-start text-xl">Klient</h2>
                <div className="flex mt-2 flex-wrap">
                    {filtersData&&filtersData.clients.map((el:string,i:number)=>(<ChipFilter 
                        label={`${el}`} 
                        key={i}
                        keyValue="clients"
                        setSelectedFilters={setSelectedFilters}
                        selectedFilters={selectedFilters}
                    />))}
                </div>
            </div>

            <div className="w-full border my-2"></div>
            <div className="flex flex-col">
                <h2 className="text-start text-xl">Grupa</h2>
                <div className="flex mt-2 flex-wrap">
                    {filtersData&&filtersData.groups.map((el:string,i:number)=>(<ChipFilter 
                        label={`${el}`} 
                        key={i}
                        keyValue="groups"
                        setSelectedFilters={setSelectedFilters}
                        selectedFilters={selectedFilters}
                    />))}
                </div>
            </div>
        </div>
    </div>)
}

export default FilterComponet;