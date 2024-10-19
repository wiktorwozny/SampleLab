import React, {useContext, useState} from "react";
import {Accept, useDropzone} from "react-dropzone";
import {AlertContext} from "../../contexts/AlertsContext";


const ImportFile: React.FC = () => {
    const [selectedFile, setSelectedFile] = useState<File | null>(null);
    const {setAlertDetails} = useContext(AlertContext);

    const onDrop = (acceptedFiles: File[]) => {
        // Sprawdzamy czy został wybrany tylko jeden plik
        if (acceptedFiles.length > 1) {
            setAlertDetails({isAlert: true, message: "Proszę wybrać tylko jeden plik", type: "warning"});
            return;
        }
        setSelectedFile(acceptedFiles[0]);
    };

    const handleRemoveFile = () => {
        setSelectedFile(null);  // Usuwanie wybranego pliku
    };

    const handleSubmit = async () => {
        if (!selectedFile) {
            setAlertDetails({isAlert: true, message: "Proszę wybrać plik przed wysłaniem", type: "warning"});
            return;
        }

        // Tutaj wysyłamy plik na backend
        const formData = new FormData();
        formData.append("file", selectedFile);

        try {
            const response = await fetch("/api/import", {
                method: "POST",
                body: formData,
            });
            if (response.ok) {
                setAlertDetails({isAlert: true, message: "Plik został przesłany pomyślnie", type: "success"});
            } else {
                setAlertDetails({isAlert: true, message: "Błąd podczas przesyłania pliku", type: "error"});
            }
        } catch (err) {
            setAlertDetails({isAlert: true, message: "Wystąpił błąd podczas przesyłania", type: "error"});
        }
    };

    const acceptedFileTypes: Accept = {
        "application/zip": [".zip"],
        "application/sql": [".sql"]
    };

    const {getRootProps, getInputProps} = useDropzone({
        onDrop,
        accept: acceptedFileTypes // Używamy zmiennej z odpowiednim typem
    });
    return (
        <div className="flex flex-col items-center justify-center h-full space-y-6">
            <div
                {...getRootProps()}
                className="border-2 border-dashed border-gray-300 rounded-lg p-4 flex flex-col items-center justify-center cursor-pointer"
            >
                <input {...getInputProps()} />
                <p className="text-gray-600">Przeciągnij pliki tutaj lub kliknij, aby wybrać pliki</p>
            </div>

            {selectedFile && (
                <button
                    className="bg-red-500 text-white px-4 py-2 rounded shadow hover:bg-red-600"
                    onClick={handleRemoveFile}
                >
                    Usuń plik
                </button>
            )}

            <button
                className="bg-blue-500 text-white px-4 py-2 rounded shadow hover:bg-blue-600 disabled:opacity-50"
                onClick={handleSubmit}
                disabled={!selectedFile}
            >
                Wyślij plik
            </button>
        </div>
    );
};

export default ImportFile;
