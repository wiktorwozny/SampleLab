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

type SortDirection = 'asc' | 'desc' | 'none';

interface SortRule {
    accessor: string;
    direction: SortDirection;
}

// Komponent Tabela
const Table = <T extends {}>({columns, data}: TableProps<T>) => {
    const [sortRules, setSortRules] = useState<SortRule[]>([]);

    const handleSort = (accessor: string) => {
        setSortRules((prevSortRules) => {
            const existingRule = prevSortRules.find(rule => rule.accessor === accessor);
            if (existingRule) {
                // Cykl: none -> asc -> desc -> none
                const newDirection = existingRule.direction === 'asc'
                    ? 'desc'
                    : existingRule.direction === 'desc'
                        ? 'none'
                        : 'asc';

                if (newDirection === 'none') {
                    // Jeśli stan wraca do 'none', usuń tę zasadę z listy
                    return prevSortRules.filter(rule => rule.accessor !== accessor);
                } else {
                    // Aktualizuj kierunek sortowania
                    return prevSortRules.map(rule =>
                        rule.accessor === accessor
                            ? {...rule, direction: newDirection}
                            : rule
                    );
                }
            } else {
                // Jeśli kolumna nie była jeszcze sortowana, dodaj ją na początek listy
                return [{accessor, direction: 'asc'}, ...prevSortRules];
            }
        });
    };

    const sortedData = [...data].sort((a, b) => {
        for (const rule of sortRules) {
            const {accessor, direction} = rule;
            if (direction === 'none') continue;

            const aValue = (a as any)[accessor];
            const bValue = (b as any)[accessor];

            if (aValue < bValue) {
                return direction === 'asc' ? -1 : 1;
            }
            if (aValue > bValue) {
                return direction === 'asc' ? 1 : -1;
            }
        }
        return 0;
    });

    return (
        <table className="table table-hover table-bordered cursor-pointer">
            <thead>
            <tr>
                {columns.map((column, index) => (
                    <th key={index} onClick={() => handleSort(column.accessor)}>
                        {column.header}
                        {/* Ikona kierunku sortowania */}
                        {(() => {
                            const rule = sortRules.find(rule => rule.accessor === column.accessor);
                            if (!rule) return ''; // Brak ikony, jeśli nie ma sortowania
                            return rule.direction === 'asc' ? ' ▲' : rule.direction === 'desc' ? ' ▼' : '';
                        })()}
                    </th>
                ))}
            </tr>
            </thead>
            <tbody>
            {sortedData.map((item, rowIndex) => (
                <tr key={rowIndex}>
                    {columns.map((column, colIndex) => (
                        <td key={colIndex}>{(item as any)[column.accessor]}</td>
                    ))}
                </tr>
            ))}
            </tbody>
        </table>
    );
};

export default Table;
