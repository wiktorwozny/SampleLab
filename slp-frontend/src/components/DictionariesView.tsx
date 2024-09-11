import React from "react";
import DictionaryButtonList from "./ui/DictionaryButton";

const buttons = [
    {label: 'Button 1', onClick: () => alert('Button 1 clicked')},
    {label: 'Button 2', onClick: () => alert('Button 2 clicked')},
    {label: 'Button 3', onClick: () => alert('Button 3 clicked')},
];

const DictionariesView: React.FC = () => {
    return (
        <div className="indications-list flex flex-col items-center h-fit justify-center w-full">
            <div>
                <h1 className="text-center font-bold text-3xl w-full my-2">Słowniki</h1>
            </div>
            <DictionaryButtonList buttons={buttons}/>
        </div>
    );
}

export default DictionariesView