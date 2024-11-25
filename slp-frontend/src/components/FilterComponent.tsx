import {IoClose} from "react-icons/io5";
import {ChipFilter} from "./ui/ChipFilter";
import {useState} from "react";
import {ProgressStateEnumDesc} from "../utils/enums";
import {ProgressState} from "../utils/types";

const FilterComponent = ({setIsFilters, isFilters, filtersData, selectedFilters, setSelectedFilters}: any) => {
    const [isOpen, setIsOpen] = useState<boolean>(true);
    return (<div className="z-10 fixed w-full h-full top-0 flex justify-end left-0">
        <div className={`absolute bg-gray-500 opacity-50 w-full h-full z-0`}
             onClick={() => {
                 setIsOpen(false)
                 setTimeout(() => {
                     setIsFilters(false)
                 }, 75)
             }
             }/>
        <div
            className={`${isOpen ? "left-0" : "left-1/3"} w-1/3 z-10 bg-white relative flex flex-col duration-75 font-bold p-3 overflow-y-scroll`}>
            <div className="w-full justify-between flex">
                <div className="text-2xl">Filtruj</div>
                <div className="text-3xl"
                     onClick={() => {
                         setIsOpen(false)
                         setTimeout(() => {
                             setIsFilters(false)
                         }, 75)
                     }
                     }>
                    <IoClose className="text-3xl cursor-pointer"/>
                </div>
            </div>

            <div className="w-full border my-2"></div>
            <div className="flex flex-col">
                <h2 className="text-start text-xl">Kod próbki</h2>
                <div className="flex mt-2 flex-wrap">
                    {filtersData && filtersData.codes.map((el: string, i: number) => (<ChipFilter
                        label={`${el}`}
                        key={i}
                        keyValue="codes"
                        setSelectedFilters={setSelectedFilters}
                        selectedFilters={selectedFilters}
                        value={`${el}`}
                    />))}
                </div>
            </div>

            <div className="w-full border my-2"></div>
            <div className="flex flex-col">
                <h2 className="text-start text-xl">Klient</h2>
                <div className="flex mt-2 flex-wrap">
                    {filtersData && filtersData.clients.map((el: string, i: number) => (<ChipFilter
                        label={`${el}`}
                        key={i}
                        keyValue="clients"
                        setSelectedFilters={setSelectedFilters}
                        selectedFilters={selectedFilters}
                        value={`${el}`}
                    />))}
                </div>
            </div>

            <div className="w-full border my-2"></div>
            <div className="flex flex-col">
                <h2 className="text-start text-xl">Grupa</h2>
                <div className="flex mt-2 flex-wrap">
                    {filtersData && filtersData.groups.map((el: string, i: number) => (<ChipFilter
                        label={`${el}`}
                        key={i}
                        keyValue="groups"
                        setSelectedFilters={setSelectedFilters}
                        selectedFilters={selectedFilters}
                        value={`${el}`}
                    />))}
                </div>
            </div>

            <div className="w-full border my-2"></div>
            <div className="flex flex-col">
                <h2 className="text-start text-xl">Status</h2>
                <div className="flex mt-2 flex-wrap">
                    {ProgressStateEnumDesc.map((el: ProgressState, i: number) => (<ChipFilter
                        label={`${el.label}`}
                        key={i}
                        keyValue="progressStatuses"
                        setSelectedFilters={setSelectedFilters}
                        selectedFilters={selectedFilters}
                        value={`${el.value}`}
                    />))}
                </div>
            </div>
        </div>
    </div>)
}

export default FilterComponent;