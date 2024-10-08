import React, {useEffect, useState} from 'react';
import {Button, Modal} from 'react-bootstrap';

interface ModalSelectionProps {
    title: string;
    options: { id: number, name: string }[];
    selectedOptions: number[];
    show: boolean;
    handleClose: () => void;
    handleSave: (selected: number[]) => void;
}

export const ModalSelection: React.FC<ModalSelectionProps> = ({
                                                                  title,
                                                                  options,
                                                                  selectedOptions,
                                                                  show,
                                                                  handleClose,
                                                                  handleSave,
                                                              }) => {
    const [selected, setSelected] = useState<number[]>([]);

    useEffect(() => {
        if (show) {
            setSelected(selectedOptions);
        }
    }, [show, selectedOptions]);

    const handleCheckboxChange = (id: number) => {
        if (selected.includes(id)) {
            setSelected(selected.filter(selectedId => selectedId !== id));
        } else {
            setSelected([...selected, id]);
        }
    };

    const handleSaveClick = () => {
        handleSave(selected);
        handleClose();
    };

    return (
        <Modal show={show} onHide={handleClose} centered>
            <Modal.Header closeButton>
                <Modal.Title>{title}</Modal.Title>
            </Modal.Header>
            <Modal.Body>
                <div className="max-h-96 overflow-y-auto">
                    <table className="min-w-full table-auto border-collapse border border-gray-300">
                        <thead>
                        <tr className="bg-gray-100">
                            <th className="p-2 border border-gray-300">#</th>
                            <th className="p-2 border border-gray-300 text-left">Nazwa</th>
                            <th className="p-2 border border-gray-300">Zaznacz</th>
                        </tr>
                        </thead>
                        <tbody>
                        {options.map((option, index) => (
                            <tr
                                key={option.id}
                                className={`hover:bg-gray-100 ${index % 2 === 0 ? 'bg-white' : 'bg-gray-50'}`}
                            >
                                <td className="p-2 border border-gray-300 text-center">{index + 1}</td>
                                <td className="p-2 border border-gray-300">{option.name}</td>
                                <td className="p-2 border border-gray-300 text-center">
                                    <input
                                        type="checkbox"
                                        className="h-4 w-4"
                                        checked={selected.includes(option.id)}
                                        onChange={() => handleCheckboxChange(option.id)}
                                    />
                                </td>
                            </tr>
                        ))}
                        </tbody>
                    </table>
                </div>
            </Modal.Body>
            <Modal.Footer>
                <Button variant="secondary" onClick={handleClose}>
                    Anuluj
                </Button>
                <Button variant="primary" onClick={handleSaveClick}>
                    Zapisz
                </Button>
            </Modal.Footer>
        </Modal>
    );
};

