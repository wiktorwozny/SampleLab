import React from "react";
import DictionaryButtonList from "../ui/DictionaryButton";
import {NavigateFunction, useNavigate} from "react-router-dom";

const DictionariesView: React.FC = () => {
    const navigate: NavigateFunction = useNavigate();

    const buttons = [
        {label: 'Klienci', onClick: () => navigate('/dictionary/clientDict')},
        {label: 'Oznaczenia', onClick: () => navigate('/dictionary/indicationDict')},
        {label: 'Normy pobrania próbek', onClick: () => navigate('/dictionary/samplingStandardDict')},
        {label: 'Kontrole', onClick: () => navigate('/dictionary/inspectionDict')},
        {label: 'Kody próbek', onClick: () => navigate('/dictionary/codeDict')},
        {label: 'Grupy', onClick: () => navigate('/dictionary/productGroupDict')},
    ];

    return (
        <div className="indications-list flex flex-col items-center w-full">
            <div>
                <h1 className="text-center font-bold text-3xl w-full my-6">Słowniki</h1>
            </div>
            <DictionaryButtonList buttons={buttons}/>
        </div>
    );
}

export default DictionariesView;
