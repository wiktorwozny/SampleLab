import { Chip } from "@mui/material"
import { useEffect, useState } from "react"
import { FiltersData } from "../../utils/types";
type FilterKey = keyof FiltersData;
interface Props {
    label: string;
    keyValue: FilterKey;
    setSelectedFilters:any;
    selectedFilters:FiltersData
}
export const ChipFilter:React.FC<Props> = ({label, keyValue, setSelectedFilters, selectedFilters})=> {
    
    const [isSelected, setIsSelected] = useState<boolean>(false);
    useEffect(()=>{
        // console.log(selectedFilters)
        // console.log(selectedFilters[keyValue])
        if(selectedFilters[keyValue].includes(label)){
            setIsSelected(true);
        }
    },[])
    const onClickHandler = () => {
        // console.log(selectedFilters)
        if(isSelected==true){
            setSelectedFilters((prev: FiltersData) => ({
                ...prev,
                [keyValue]: prev[keyValue].filter((el: string) => el !== label)
            }));
        }else{
            setSelectedFilters((prev: FiltersData) => ({
                ...prev,
                [keyValue]: [...prev[keyValue], label]
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