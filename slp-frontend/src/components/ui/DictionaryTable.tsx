import React, {useState} from 'react';
import {Column} from "../../utils/types";
import {Dropdown} from "react-bootstrap";
import {LoadingSpinner} from "./LoadingSpinner";

interface TableProps<T> {
    columns: Column<T>[];
    data: T[];
    onView: (item: T) => void;
    onEdit: (item: T) => void;
    onDelete: (item: T) => void;
}

type SortDirection = 'asc' | 'desc' | 'none';

interface SortRule<T> {
    accessor: keyof T | string;
    direction: SortDirection;
}

const DictionaryTable = <T extends {}>({columns, data, onView, onEdit, onDelete}: TableProps<T>) => {
    const [sortRules, setSortRules] = useState<SortRule<T>[]>([]);

    const handleSort = (accessor: keyof T | string) => {
        setSortRules((prevSortRules) => {
            const existingRule = prevSortRules.find(rule => rule.accessor === accessor);
            if (existingRule) {
                const newDirection = existingRule.direction === 'asc'
                    ? 'desc'
                    : existingRule.direction === 'desc'
                        ? 'none'
                        : 'asc';

                if (newDirection === 'none') {
                    return prevSortRules.filter(rule => rule.accessor !== accessor);
                } else {
                    return prevSortRules.map(rule =>
                        rule.accessor === accessor
                            ? {...rule, direction: newDirection}
                            : rule
                    );
                }
            } else {
                return [{accessor, direction: 'asc'}, ...prevSortRules];
            }
        });
    };

    const sortedData = [...data].sort((a, b) => {
        for (const rule of sortRules) {
            const {accessor, direction} = rule;
            if (direction === 'none') continue;

            const column = columns.find(col => col.accessor === accessor);
            if (!column) continue;

            const aValue = a[accessor as keyof T];
            const bValue = b[accessor as keyof T];
            if (aValue !== undefined && bValue !== undefined && aValue !== null && bValue !== null) {
                if (aValue < bValue) {
                    return direction === 'asc' ? -1 : 1;
                }
                if (aValue > bValue) {
                    return direction === 'asc' ? 1 : -1;
                }
            }
        }
        return 0;
    });

    return (
        <div className="justify-content-center flex mb-2 min-w-1/2">
            {data.length === 0 ? (
                <LoadingSpinner/>
            ) : (
                <table className="table table-hover table-bordered max-w-fit">
                    <thead>
                    <tr>
                        <th className="w-20">Akcja</th>
                        {columns.map((column, index) => {
                            const rule = sortRules.find(rule => rule.accessor === column.accessor);
                            const isSorted = rule && rule.direction !== 'none';

                            return (
                                <th
                                    scope="col"
                                    key={index}
                                    className={`text-left ${isSorted ? 'bg-gray-400' : 'bg-gray-300'}`}
                                    onClick={() => handleSort(column.accessor)}
                                >
                                    {column.header}
                                    {isSorted ? (rule?.direction === 'asc' ? ' ▲' : ' ▼') : ''}
                                </th>
                            );
                        })}
                    </tr>
                    </thead>
                    <tbody>
                    {sortedData.map((item, rowIndex) => (
                        <tr key={rowIndex}>
                            <td>
                                <Dropdown>
                                    <Dropdown.Toggle
                                        className="bg-sky-500 border-sky-500 hover:bg-sky-600 hover:border-sky-600"
                                        id="dropdown-basic">
                                        ☰
                                    </Dropdown.Toggle>

                                    <Dropdown.Menu>
                                        <Dropdown.Item onClick={() => onView(item)}>Szczegóły</Dropdown.Item>
                                        <Dropdown.Item onClick={() => onEdit(item)}>Edycja</Dropdown.Item>
                                        <Dropdown.Item onClick={() => onDelete(item)}>Usuń</Dropdown.Item>
                                    </Dropdown.Menu>
                                </Dropdown>
                            </td>
                            {columns.map((column, colIndex) => (
                                <td className="text-left" key={colIndex}>
                                    {column.render
                                        ? column.render(item[column.accessor as keyof T])
                                        : String(item[column.accessor as keyof T])}
                                </td>
                            ))}
                        </tr>
                    ))}
                    </tbody>
                </table>
            )}
        </div>
    );
};

export default DictionaryTable;
