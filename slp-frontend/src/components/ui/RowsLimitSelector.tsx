import React from "react";

interface RowsLimitSelectorProps {
    rowsLimit: number;
    setRowsLimit: (limit: number) => void;
}

const RowsLimitSelector: React.FC<RowsLimitSelectorProps> = ({
                                                                 rowsLimit,
                                                                 setRowsLimit,
                                                             }) => {
    return (
        <div className="flex items-center justify-center mb-4">
            <label
                htmlFor="rowsLimit"
                className="text-sm font-medium text-gray-700"
            >
                Liczba wierszy:
            </label>
            <select
                id="rowsLimit"
                value={rowsLimit}
                onChange={(e) => setRowsLimit(Number(e.target.value))}
                className="ml-2 block w-24 rounded-md border-gray-300 shadow-sm focus:border-indigo-500 focus:ring focus:ring-indigo-200 focus:ring-opacity-50"
            >
                {[5, 10, 20, 50].map((num) => (
                    <option key={num} value={num}>
                        {num}
                    </option>
                ))}
            </select>
        </div>
    );
};

export default RowsLimitSelector;
