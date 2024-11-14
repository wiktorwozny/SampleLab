import SampleList from "../components/SampleList"
import {HiAdjustmentsHorizontal} from "react-icons/hi2";
import {useEffect, useState} from "react";
import FilterComponent from "../components/FilterComponent";
import {FiltersData} from "../utils/types";
import {getFiltersData} from "../helpers/dataApi";
import {checkResponse} from "../utils/checkResponse";
import Title from "../components/ui/Title";

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

    return (
        <div className="w-full">
            <Title message={'Lista póbek'}/>
            <div className="w-full justify-end flex">
                <div
                    className=" rounded-3 bg-sky-500 hover:bg-sky-500 relative mr-4 mb-2 p-2 flex items-center cursor-pointer"
                    onClick={() => {
                        setIsFilters(true)
                    }}>
                    <div className="text-white">Filtruj &nbsp;</div>
                    <HiAdjustmentsHorizontal className="text-white text-3xl"></HiAdjustmentsHorizontal>
                </div>
            </div>
            <SampleList selectedFilters={selectedFilters}/>
            {isFilters && <FilterComponent
                setIsFilters={setIsFilters}
                isFilters={isFilters}
                filtersData={filtersData}
                selectedFilters={selectedFilters}
                setSelectedFilters={setSelectedFilters}
            />}
        </div>)
}

export default SampleListPage;