import React, {useState} from "react";

interface SearchableDropdownProps {
    options: { id: number, name: string }[];
    selectedOption: number | null;
    onSelect: (id: number | null) => void;
    disabled: boolean;
}

const SearchableDropdown: React.FC<SearchableDropdownProps> = ({options, selectedOption, onSelect, disabled}) => {
    const [searchTerm, setSearchTerm] = useState('');
    const [isOpen, setIsOpen] = useState(false);

    const filteredOptions = options.filter(option =>
        option.name.toLowerCase().includes(searchTerm.toLowerCase())
    );

    const handleSelect = (id: number) => {
        onSelect(id);
        setIsOpen(false);
    };

    const selectedOptionName = options.find(option => option.id === selectedOption)?.name || 'Select option';

    return (
        <div className="relative w-full">
            <div className="border border-gray-300 rounded-md p-2 cursor-pointer" onClick={() => setIsOpen(!isOpen)}>
                <span>{selectedOptionName}</span>
            </div>

            {isOpen && (
                <div className="absolute w-full mt-1 bg-white border border-gray-300 rounded-md shadow-lg z-10">
                    <input
                        type="text"
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                        className="w-full p-2 border-b border-gray-300"
                        placeholder="Szukaj..."
                    />
                    <ul className="max-h-60 overflow-y-auto">
                        {filteredOptions.map(option => (
                            <li
                                key={option.id}
                                className="p-2 hover:bg-gray-100 cursor-pointer"
                                onClick={() => handleSelect(option.id)}
                            >
                                {option.name}
                            </li>
                        ))}
                    </ul>
                </div>
            )}
        </div>
    );
};

export default SearchableDropdown;
