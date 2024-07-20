import React from 'react';

interface DictionaryButtonProps {
    label: string;
    onClick: () => void;
}

const DictionaryButton: React.FC<DictionaryButtonProps> = ({label, onClick}) => {
    return (
        <button
            className="bg-blue-500 text-white py-2 px-4 rounded-md transition duration-300 ease-in-out hover:bg-blue-700"
            onClick={onClick}>
            {label}
        </button>
    );
};

interface DictionaryButtonListProps {
    buttons: { label: string; onClick: () => void }[];
}

const DictionaryButtonList: React.FC<DictionaryButtonListProps> = ({buttons}) => {
    return (
        <div className="flex flex-wrap gap-4">
            {buttons.map((button, index) => (
                <DictionaryButton key={index} label={button.label} onClick={button.onClick}/>
            ))}
        </div>
    );
};

export default DictionaryButtonList;