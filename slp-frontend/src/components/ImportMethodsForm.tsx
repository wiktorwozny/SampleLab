import {useContext, useState} from 'react';
import {Accept, useDropzone} from "react-dropzone";
import {AlertContext} from '../contexts/AlertsContext';
import {importMethods} from '../helpers/methodsApi';
import {DisableButton} from "./ui/StandardButton";
import {FaFileAlt} from 'react-icons/fa';

const ImportMethodsForm = () => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const {setAlertDetails} = useContext(AlertContext);

    const onDrop = (acceptedFiles: File[]) => {
        setSelectedFile(acceptedFiles[0]);
    };

    const handleSubmit = async () => {
        if (!selectedFile) {
            setAlertDetails({isAlert: true, message: "Proszę wybrać plik przed wysłaniem", type: "warning"});
            return;
        }

        try {
            let response = await importMethods(selectedFile);
            console.log(response);
            if (response.status === 200) {
                setAlertDetails({type: "success", isAlert: true, message: "Udało się wczytać plik"});
            }
        } catch (err: any) {
            console.log(err);
            setAlertDetails({type: "error", isAlert: true, message: "Nie udało się wczytać pliku"});
        }
    };

    const acceptedFileTypes: Accept = {
        "application/xlsm": [".xlsm"]
    };

    const {getRootProps, getInputProps} = useDropzone({
        onDrop,
        accept: acceptedFileTypes,
        maxFiles: 1
    });

    const handleRemoveFile = () => {
        setSelectedFile(null); // Usuwanie wybranego pliku
    };

    return (
        <div className="mt-20 flex flex-col items-center justify-center h-full space-y-6">
            <h1 className="text-3xl font-semibold text-center mb-4">Wczytaj metody</h1>
            <div
                {...getRootProps()}
                className="border-2 border-dashed border-gray-300 rounded-lg p-4 flex flex-col items-center justify-center cursor-pointer hover:border-gray-500"
            >
                <input {...getInputProps()} />
                <p className="text-gray-600 text-center">
                    Przeciągnij plik lub kliknij tutaj
                    <br/>
                    (format pliku: *.xlsm)
                </p>
            </div>

            {selectedFile && (
                <div className="flex items-center space-x-4 bg-gray-100 p-4 rounded-lg shadow">
                    <FaFileAlt className="text-blue-500 text-4xl"/>
                    <div className="flex flex-col">
                        <span className="text-gray-800 font-semibold">{selectedFile.name}</span>
                        <span className="text-gray-600 text-sm">
                            {(selectedFile.size / 1024).toFixed(2)} KB
                        </span>
                    </div>
                    <button
                        className="ml-auto bg-red-500 text-white px-4 py-2 rounded shadow hover:bg-red-600"
                        onClick={handleRemoveFile}
                    >
                        Usuń
                    </button>
                </div>
            )}

            <DisableButton
                type="button"
                onClick={handleSubmit}
                disabled={!selectedFile}
            >
                Wyślij plik
            </DisableButton>
        </div>
    );
};

export default ImportMethodsForm;
