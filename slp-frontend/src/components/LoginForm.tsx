import { useForm } from 'react-hook-form';
import { Input } from './ui/Input';
import { FormLabel } from './ui/Labels';
import { StandardButton } from './ui/StandardButton';
import { useNavigate } from 'react-router-dom';
import { loginRequest } from '../helpers/userApi';
import { useContext } from 'react';
import { AlertContext } from '../contexts/AlertsContext';

const LoginForm = () => {
    const method = useForm();
    const {handleSubmit, register, formState: {errors}} = method
    const {setAlertDetails} = useContext(AlertContext)
    // const navigate = useNavigate();

    const loginFunction = async (values: any) => {
        try {
            let response = await loginRequest(values)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                console.log("udalo ci się zalogować")
                console.log(response.data)
                setAlertDetails({type: "success", isAlert: true, message: "Udało ci się pomyślnie zalogować"})
                localStorage.setItem('role', response.data.user.role);
                localStorage.setItem('token', response.data.token);
                window.dispatchEvent(new Event('change'));
                // navigate("/")
            }
        } catch (err: any) {
            if(err?.response?.data?.message === "User not found" || err?.response?.data?.message === "Wrong password") {
                setAlertDetails({type: "error", isAlert: true, message: "Błędny e-mail lub hasło"})
            } else {
                setAlertDetails({type: "error", isAlert: true, message: "Nie udało się zalogować. Spróbuj ponownie później"})
            }
            
            console.log(err)
        }
    }

    return(<div className="w-full flex items-center flex-col justify-center relative h-screen">
            <h2 className="font-bold my-3 text-2xl">Zaloguj się</h2>
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