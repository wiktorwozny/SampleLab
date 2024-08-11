import { useForm } from 'react-hook-form';
import { Input } from './ui/Input';
import { FormLabel } from './ui/Labels';
import { StandardButton } from './ui/StandardButton';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../helpers/userApi';

const LoginForm = () => {
    const method = useForm();
    const {handleSubmit, register, formState: {errors}} = method
    const navigate = useNavigate();

    const loginFunction = async (values: any) => {
        try {
            let response = await loginRequest(values)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                console.log("udalo ci się zalogować")
                navigate("/")
            }
        } catch (err) {
            console.log(err)
        }
    }

    return(<div className="w-full flex items-center flex-col justify-center relative">
            <h2 className="font-bold my-3 text-2xl">Formularz Logowania</h2>
            <form className='flex flex-col w-1/3 mt-3' onSubmit={handleSubmit(loginFunction)}>
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

                <FormLabel className='text-start mt-3'>Hasło</FormLabel>
                <Input
                    type="password"
                    className="my-custom-class"
                    {...register("password", {
                        required: {
                            value: true,
                            message: "Pole wymagane"
                        }
                    })}
                />
                {errors.password && errors.password.message &&
                    <p className="text-red-600 text-start">{`${errors.password.message}`}</p>}

                <StandardButton type="submit" className='mt-3 w-full'>Zaloguj</StandardButton>
            </form>
    </div>)
}

export default LoginForm;