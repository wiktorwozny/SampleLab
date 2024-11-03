import { useForm } from 'react-hook-form';
import { StandardButton } from './ui/StandardButton';
import { useContext } from 'react';
import { AlertContext } from '../contexts/AlertsContext';
import { importMethods } from '../helpers/methodsApi';

const ImportMethodsForm = () => {
    const method = useForm();
    const {handleSubmit, register, formState: {errors}} = method
    const {setAlertDetails} = useContext(AlertContext)

    const importMethodsFunction = async (values: any) => {
        try {
            let response = await importMethods(values)
            console.log(response)
            if (response.status === 200) {
                setAlertDetails({type: "success", isAlert: true, message: "Udało ci się wczytać metody"})
            }
        } catch (err: any) {
            console.log(err)
            setAlertDetails({type: "error", isAlert: true, message: "Nieprawidłowy format pliku"})            
        }
    }

    return(<div className="w-full flex items-center flex-col justify-center relative h-screen">
            <h2 className="font-bold my-3 text-2xl">Wczytaj metody</h2>
            <form className='flex flex-col w-1/3 mt-3' onSubmit={handleSubmit(importMethodsFunction)}>
                <input
                    className="my-custom-class"
                    type="file"
                    {...register("file", {
                        required: {
                            value: true,
                            message: "Wybierz plik"
                        }
                    })}
                />
                {errors.file && errors.file.message &&
                    <p className="text-red-600 text-start">{`${errors.file.message}`}</p>}

                <StandardButton type="submit" className='mt-3 w-full'>Wczytaj</StandardButton>
            </form>
    </div>)
};

export default ImportMethodsForm;
