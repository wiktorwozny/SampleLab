import React from "react";
import DictionaryButtonList from "../components/ui/DictionaryButton";
import {NavigateFunction, useNavigate} from "react-router-dom";
import Title from "../components/ui/Title";

const DictionariesPage: React.FC = () => {
    const navigate: NavigateFunction = useNavigate();

    const buttons = [
        {label: 'Klienci', onClick: () => navigate('/dictionary/clientDict')},
        // {label: 'Oznaczenia', onClick: () => navigate('/dictionary/indicationDict')},
        {label: 'Normy pobrania próbki', onClick: () => navigate('/dictionary/samplingStandardDict')},
        {label: 'Kontrole', onClick: () => navigate('/dictionary/inspectionDict')},
        {label: 'Kody próbki', onClick: () => navigate('/dictionary/codeDict')},
        {label: 'Grupy', onClick: () => navigate('/dictionary/productGroupDict')},
        {label: 'Asortyment', onClick: () => navigate('/dictionary/assortmentDict')},
    ];

    return (
        <div>

            <Title message={'Edycja danych'}/>

            <div className="indications-list flex flex-col items-center w-full">

               
                <DictionaryButtonList buttons={buttons}/>
            </div>
        </div>
    );
}

export default DictionariesPage;
