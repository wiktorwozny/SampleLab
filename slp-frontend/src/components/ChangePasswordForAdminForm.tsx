import { useForm } from "react-hook-form";
import { FormLabel } from "./ui/Labels";
import { Input } from "./ui/Input";
import { StandardButton } from "./ui/StandardButton";
import { changePasswordForAdmin } from "../helpers/userApi";
import { checkResponse } from "../utils/checkResponse";
import { useContext } from "react";
import { AlertContext } from "../contexts/AlertsContext";
import { Modal } from "react-bootstrap";
const ChangePasswordFormAdminForm = ({email, closeHandler}:{email:String|null, closeHandler:React.Dispatch<React.SetStateAction<String | null>>}) => {
    const method = useForm();
    const {handleSubmit, register, watch, formState: {errors}} = method;
    const password = watch("newPassword")
    const {setAlertDetails} = useContext(AlertContext)
    const changePasswordHandler = async(values:any) => {
        console.log(values)
        try{
            if(email != null){
                let response = await changePasswordForAdmin(email, values);
                if(response.status===200){
                    closeHandler(null);
                    setAlertDetails({isAlert: true, message: "Udało się zmienić hasło", type: "success"})
                }
            }else{
                setAlertDetails({isAlert: true, message: "Nie udało się zmienić hasło. Spróbuj ponownie później", type: "error"})
            }
        }catch(err){
            checkResponse(err);
            setAlertDetails({isAlert: true, message: "Nie udało się zmienić hasło. Spróbuj ponownie później", type: "error"})
        }
    }
    return(<div className="flex flex-col justify-center">
        <Modal.Header closeButton>
                <Modal.Title>
                    {"Zmień hasło"}
                </Modal.Title>
        </Modal.Header>
        <Modal.Body>
            <form className='flex flex-col mt-3' onSubmit={handleSubmit(changePasswordHandler)}>
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
                    className="my-custom-class w-full"
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
                            if (val !== password) {
                                return "Podane hasła nie są takie same"
                            }
                            return true;
                        }
                    })}
                />
                {errors.repeatedPassword && errors.repeatedPassword.message &&
                    <p className="text-red-600 text-start">{`${errors.repeatedPassword.message}`}</p>}
            </form>
        </Modal.Body>
        <Modal.Footer>
            <div className="flex justify-end">
                <StandardButton type="button" className='mr-2' onClick={handleSubmit(changePasswordHandler)}>Zmień hasło</StandardButton>
                <StandardButton type="button" className='' onClick={()=>closeHandler(null)}>Anuluj</StandardButton>
            </div>
        </Modal.Footer>
    </div>)
}

export default ChangePasswordFormAdminForm;