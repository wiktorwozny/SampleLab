import { FormProvider, useForm } from 'react-hook-form';
import { Input } from './ui/Input';
import { FormLabel } from './ui/Labels';
import { StandardButton } from './ui/StandardButton';
import { useNavigate } from 'react-router-dom';
import { FormSelect } from './ui/Select';
import { RoleEnumDesc } from '../utils/enums';
const RegisterForm = () => {
    const method = useForm();
    const {handleSubmit, register, formState: {errors}} = method
    const navigate = useNavigate();

    const registerFunction = (values:any) => {
        console.log(values)
    }

    return(<div className="w-full flex items-center flex-col relative justify-center">
        <h2 className="font-bold my-3 text-2xl">Formularz Rejestracji</h2>
        <FormProvider {...method}>
            <form className='flex flex-col w-1/3 mt-3' onSubmit={handleSubmit(registerFunction)}>
                <FormLabel className='text-start'>Pełne imie</FormLabel>
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
                    {...register("email", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                />
                {errors.email && errors.email.message &&
                    <p className="text-red-600 text-start">{`${errors.email.message}`}</p>}

                <FormLabel className='text-start mt-3'>Wybierz role</FormLabel>
                <FormSelect
                    options={RoleEnumDesc}
                    {...register("role",{
                        required:{
                        value:true,
                        message:"Pole wymagane"
                    }})}
                />
                {errors.role && errors.role.message &&
                    <p className="text-red-600 text-start">{`${errors.role.message}`}</p>}

                <StandardButton type="submit" className='mt-3 w-full'>Zaloguj</StandardButton>
            </form>
        </FormProvider>
</div>)
}

export default RegisterForm;