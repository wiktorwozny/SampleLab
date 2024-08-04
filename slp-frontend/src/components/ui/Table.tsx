import React, {useState} from 'react';

// Definiujemy typy dla kolumn i danych
interface Column {
    header: string;
    accessor: string;
}

interface TableProps<T> {
    columns: Column[];
    data: T[];
}

// Komponent Tabela
const Table = <T extends {}>({columns, data}: TableProps<T>) => {
    const [activeColumn, setActiveColumn] = useState<string>('id');

    return (
        <div className="w-full">

            <table className="table table-hover table-bordered cursor-pointer">
                <thead>
                <tr>
                    {columns.map((column, index) => (
                        <th scope="col" className={activeColumn === 'id' ? '!bg-gray-400' : '!bg-gray-300'}
                            key={index}>{column.header}</th>
                    ))}
                </tr>
                </thead>
                <tbody>
                {data.map((item, rowIndex) => (
                    <tr key={rowIndex}>
                        {columns.map((column, colIndex) => (
                            <td key={colIndex}>{(item as any)[column.accessor]}</td>
                        ))}
                    </tr>
                ))}
                </tbody>
            </table>
        </div>
    );
};

export default Table;
