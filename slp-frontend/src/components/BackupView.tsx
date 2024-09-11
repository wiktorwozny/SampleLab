import React, {useContext} from "react";
import {backup} from "../helpers/backupApi";
import {AlertContext} from "../contexts/AlertsContext";


const BackupView: React.FC<{}> = () => {

    const {setAlertDetails} = useContext(AlertContext);
    const formatDate = (date: Date) => {
        return date.toLocaleDateString('pl-PL', {
            day: '2-digit',
            month: '2-digit',
            year: 'numeric'
        });
    };

    const handleBackup = async (mode: string) => {
        try {
            let response = await backup(mode)
            console.log(response)
            setAlertDetails({isAlert: true, message: "Archiwizacja danych zakończona poprawnie", type: "success"})
            if (response != null) {
                const url = window.URL.createObjectURL(new Blob([response.data]));
                const a = document.createElement('a');
                a.href = url;
                if (mode === "CSV") {
                    a.download = `backup_folder_with_csv_${formatDate(new Date())}.zip`;
                } else {
                    a.download = `backup_${formatDate(new Date())}_.sql`;
                }
                document.body.appendChild(a);
                a.click();
                a.remove();
                window.URL.revokeObjectURL(url);
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})

        }
    }

    return (
        <div className="indications-list flex flex-col items-center h-fit justify-center w-full">
            <div>
                <h1 className="text-center font-bold text-3xl w-full my-2">Opcje archiwizacji bazy danych</h1>
            </div>
            <div className="flex space-x-4">
                <button className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 focus:outline-none"
                        onClick={() => handleBackup('FULL_BACKUP')}
                >
                    Pełna archiwizacja
                </button>
                <button className="px-4 py-2 bg-blue-500 text-white rounded hover:bg-green-600 focus:outline-none"
                        onClick={() => handleBackup('DATA_ONLY')}
                >
                    Archiwizacja "data-only"
                </button>
                <button className="px-4 py-2 bg-green-500 text-white rounded hover:bg-green-600 focus:outline-none"
                        onClick={() => handleBackup('CSV')}
                >
                    Archiwizacja do plików *.csv
                </button>
            </div>
        </div>
    )
}

export default BackupView;
