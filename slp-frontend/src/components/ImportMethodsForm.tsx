import {useContext, useState} from 'react';
import {Accept, useDropzone} from "react-dropzone";
import {AlertContext} from '../contexts/AlertsContext';
import {importMethods} from '../helpers/methodsApi';
import {DisableButton} from "./ui/StandardButton";

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
            let response = await importMethods(selectedFile)
            console.log(response)
            if (response.status === 200) {
                setAlertDetails({type: "success", isAlert: true, message: "Udało się wczytać plik"})
            }
        } catch (err: any) {
            console.log(err)
            setAlertDetails({type: "error", isAlert: true, message: "Nie udało się wczytać pliku"})
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
    return (
        <div className="mt-20 flex flex-col items-center justify-center h-full space-y-6">
            <h1 className="text-3xl font-semibold text-center mb-4">Wczytaj metody</h1>
            <div
                {...getRootProps()}
                className="border-2 border-dashed border-gray-300 rounded-lg p-4 flex flex-col items-center justify-center cursor-pointer"
            >
                <input {...getInputProps()} />
                <p className="text-gray-600">Przeciągnij plik lub kliknij tutaj
                    <br/>
                    (format pliku: *.xlsm)
                </p>
            </div>

            {selectedFile && (
                <p className="text-gray-700">Wybrano plik: {selectedFile.name}</p>
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
