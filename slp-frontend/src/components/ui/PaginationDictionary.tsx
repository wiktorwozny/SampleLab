import React from 'react';

interface PaginationProps {
    currentPage: number;
    totalPages: number;
    onPageChange: (page: number) => void;
}

const PaginationDictionary: React.FC<PaginationProps> = ({currentPage, totalPages, onPageChange}) => {
    return (
        <div className="flex justify-center items-center my-2 space-x-4">
            <button
                className="px-3 py-2 bg-gray-200 text-gray-700 rounded-full shadow-sm hover:bg-gray-300 disabled:opacity-50 disabled:cursor-not-allowed"
                onClick={() => onPageChange(currentPage - 1)}
                disabled={currentPage === 1}
            >
                ←
            </button>
            <span className="text-gray-700 font-medium">
                Strona {currentPage} z {totalPages}
            </span>
            <button
                className="px-3 py-2 bg-gray-200 text-gray-700 rounded-full shadow-sm hover:bg-gray-300 disabled:opacity-50 disabled:cursor-not-allowed"
                onClick={() => onPageChange(currentPage + 1)}
                disabled={currentPage === totalPages}
            >
                →
            </button>
        </div>
    );
};

export default PaginationDictionary;
