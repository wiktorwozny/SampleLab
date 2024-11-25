import React from 'react';
import {StandardButton} from "./StandardButton";

interface DictionaryButtonProps {
    label: string;
    onClick: () => void;
}

const DictionaryButton: React.FC<DictionaryButtonProps> = ({label, onClick}) => {
    return (
        <StandardButton
            type="button"
            className="bg-blue-500 text-white py-4 px-8 rounded-md shadow-md transition duration-300 ease-in-out hover:bg-blue-600 transform hover:scale-105 text-center"
            onClick={onClick}
        >
            {label}
        </StandardButton>
    );
};

interface DictionaryButtonListProps {
    buttons: { label: string; onClick: () => void }[];
}

const DictionaryButtonList: React.FC<DictionaryButtonListProps> = ({buttons}) => {
    return (
        <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 w-full max-w-screen-lg">
            {buttons.map((button, index) => (
                <DictionaryButton key={index} label={button.label} onClick={button.onClick}/>
            ))}
        </div>
    );
};

export default DictionaryButtonList;
