import { FormProvider, useForm } from 'react-hook-form';
import { Input } from './ui/Input';
import { FormLabel } from './ui/Labels';
import { StandardButton } from './ui/StandardButton';
import { ExaminationFromSelect } from './ui/Select';
import { RoleEnumDesc } from '../utils/enums';
import { registerRequest } from '../helpers/userApi';
import { useContext } from 'react';
import {AlertContext} from '../contexts/AlertsContext';
import {checkResponse} from '../utils/checkResponse';

type Props = {
    setPassword: (password: string) => void
}

const RegisterForm = ({setPassword}: Props) => {
    const method = useForm();
    const {handleSubmit, register, formState: {errors}} = method
    const {setAlertDetails} = useContext(AlertContext);

    const registerFunction = async (values: any) => {
        try {
            let response = await registerRequest(values)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setPassword(response.data.password)
                setAlertDetails({isAlert: true, type: "success", message: "Utworzyłeś nowe konto"})
            }
        } catch (err: any) {
            if (err?.response?.data?.message === "Account already exists") {
                setAlertDetails({isAlert: true, type: "error", message: "Ten email jest już wykorzystany"})
            } else {
                setAlertDetails({
                    isAlert: true,
                    type: "error",
                    message: "Nie udało się stworzyć konta. Spróbuj później"
                })
            }
            console.log(err)
            checkResponse(err);
        }
    }

    return (
        <div className="w-full flex items-center flex-col relative justify-center">
            <FormProvider {...method}>
                <form className='flex flex-col w-1/3 mt-3' onSubmit={handleSubmit(registerFunction)}>
                    <FormLabel className='text-start'>Imię i nazwisko</FormLabel>
                    <Input
                        className="my-custom-class"
                        {...register("name", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            }
                        })}
                    />
                    {errors.name && errors.name.message &&
                        <p className="text-red-600 text-start">{`${errors.name.message}`}</p>}

                    <FormLabel className='text-start'>E-mail</FormLabel>
                    <Input
                        className="my-custom-class"
                        type="email"
                        {...register("email", {
                            required: {
                                value: true,
                                message: "Pole wymagane"
                            },
                            pattern: {
                                value: /^[\w-\.]+@([\w-]+\.)+[\w-]{2,4}$/,
                                message: "Zły format email"
                            }
                        })}
                    />
                    {errors.email && errors.email.message &&
                        <p className="text-red-600 text-start">{`${errors.email.message}`}</p>}

                    <FormLabel className='text-start mt-3'>Rola</FormLabel>
                    <ExaminationFromSelect
                        options={RoleEnumDesc}
                        {...register("role",{
                            required:{
                            value:true,
                            message:"Pole wymagane"
                        }})}
                    />
                    {errors.role && errors.role.message &&
                        <p className="text-red-600 text-start">{`${errors.role.message}`}</p>}

                    <StandardButton type="submit" className='mt-3 w-full'>Zarejestruj</StandardButton>
                </form>
            </FormProvider>
        </div>)
}

export default RegisterForm;