import { FormLabel } from "./ui/Labels";
import { Input } from "./ui/Input";
import { useForm } from "react-hook-form";
import { AlertContext } from "../contexts/AlertsContext";
import { useContext } from "react";
import { StandardButton } from "./ui/StandardButton";
import { useNavigate } from "react-router-dom";

const ChangePasswordForm = () => {
    const method = useForm();
    const {handleSubmit, register, formState: {errors}, watch} = method
    const {setAlertDetails} = useContext(AlertContext);
    // const naviagte = useNavigate()
    const password = watch("newPassword", "");
    const submit = (values:any) => {
        console.log(values);
        // naviagte("/")
    }

    return(<div className="w-full h-screen z-10 absolute flex justify-center items-center bg-white relative">
        <h2 className="text-3xl font-bold absolute top-10">Formularz zmiany hasła</h2>
        <form onSubmit={handleSubmit(submit)} className="w-1/3">
            <FormLabel className='text-start w-full'>Podaj aktualne hasło</FormLabel>
            <Input
                className="my-custom-class"
                type = "password"
                {...register("oldPassword", {
                    required: {
                        value: true,
                        message: "Pole wymagane"
                    }
                })}
            />
            {errors.oldPassword && errors.oldPassword.message &&
                <p className="text-red-600 text-start">{`${errors.oldPassword.message}`}</p>}
            
            <FormLabel className='text-start w-full'>Podaj nowe hasło</FormLabel>
            <Input
                className="my-custom-class"
                type = "password"
                {...register("newPassword", {
                    required: {
                        value: true,
                        message: "Pole wymagane"
                    },
                    minLength:{
                        value:8,
                        message:"Minimum 8 znaków"
                    }
                })}
            />
            {errors.newPassword && errors.newPassword.message &&
                <p className="text-red-600 text-start">{`${errors.newPassword.message}`}</p>}
            
            <FormLabel className='text-start w-full'>Powtórz wpisane hasło</FormLabel>
            <Input
                className="my-custom-class"
                type = "password"
                {...register("repeatedPassword", {
                    required: {
                        value: true,
                        message: "Pole wymagane"
                    },
                    minLength:{
                        value:8,
                        message:"Minimum 8 znaków"
                    },
                    validate: (val:string) => {
                        if (val != password) {
                            return "Podane hasła nie są takie same"
                        }
                        return true;
                    }
                })}
            />
            {errors.repeatedPassword && errors.repeatedPassword.message &&
                <p className="text-red-600 text-start">{`${errors.repeatedPassword.message}`}</p>}
            
            <StandardButton type="submit" className='mt-3 w-full'>Zmień hasło</StandardButton>
        </form>
    </div>)
}


export default ChangePasswordForm;