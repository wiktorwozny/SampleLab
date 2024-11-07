import SampleList from "../components/SampleList"
import {HiAdjustmentsHorizontal} from "react-icons/hi2";
import {useEffect, useState} from "react";
import FilterComponet from "../components/FilterComponent";
import {FiltersData} from "../utils/types";
import {getFiltersData} from "../helpers/dataApi";
import {checkResponse} from "../utils/checkResponse";

const SampleListPage = () => {
    const [isFilters, setIsFilters] = useState<boolean>(false)
    const [filtersData, setFiltersData] = useState<FiltersData | null>(null);
    const [selectedFilters, setSelectedFilters] = useState<FiltersData>({
        codes: [],
        groups: [],
        clients: [],
        progressStatuses: []
    })
    useEffect(() => {
        const getFiltersDataFunction = async () => {
            try {
                let repsonse = await getFiltersData();
                if (repsonse.status === 200) {
                    setFiltersData(repsonse.data);
                }
            } catch (err) {
                console.log(err);
                checkResponse(err);
            }
        }
        getFiltersDataFunction();
    }, [])

    return (<div className="w-full">
        <h1 className="text-center font-bold text-3xl w-full my-3">Lista próbek</h1>
        <div className="w-full justify-end flex">
            <div
                className="flex border relative mr-2 mb-2 p-2 border-black flex items-center hover:bg-gray-300 cursor-pointer"
                onClick={() => {
                    setIsFilters(true)
                }}>
                <div>Filtruj &nbsp;</div>
                <HiAdjustmentsHorizontal className="text-3xl"></HiAdjustmentsHorizontal>
            </div>
        </div>
        <SampleList selectedFilters={selectedFilters}/>
        {isFilters && <FilterComponet
            setIsFilters={setIsFilters}
            isFilters={isFilters}
            filtersData={filtersData}
            selectedFilters={selectedFilters}
            setSelectedFilters={setSelectedFilters}
        />}
    </div>)
}

export default SampleListPage;