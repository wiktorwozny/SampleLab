import { Chip } from "@mui/material"
import { useEffect, useState } from "react"
import { FiltersData } from "../../utils/types";
type FilterKey = keyof FiltersData;
interface Props {
    label: string;
    keyValue: FilterKey;
    setSelectedFilters:any;
    selectedFilters:FiltersData;
    value: string;
}
export const ChipFilter:React.FC<Props> = ({label, keyValue, setSelectedFilters, selectedFilters, value})=> {
    
    const [isSelected, setIsSelected] = useState<boolean>(false);
    useEffect(()=>{
        // console.log(selectedFilters)
        // console.log(selectedFilters[keyValue])
        if(selectedFilters[keyValue].includes(value)){
            setIsSelected(true);
        }
    },[])
    const onClickHandler = () => {
        // console.log(selectedFilters)
        if(isSelected==true){
            setSelectedFilters((prev: FiltersData) => ({
                ...prev,
                [keyValue]: prev[keyValue].filter((el: string) => el !== value)
            }));
        }else{
            setSelectedFilters((prev: FiltersData) => ({
                ...prev,
                [keyValue]: [...prev[keyValue], value]
            }));
        }
        setIsSelected(prev=>!prev)
    }
    return(<Chip 
        className="mx-2 my-2"
        label={label} 
        variant={isSelected? "filled" : "outlined"} 
        onClick={onClickHandler}
    />)
}