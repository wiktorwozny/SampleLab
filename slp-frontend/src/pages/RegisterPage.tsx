import { useState } from "react";
import RegisterForm from "../components/RegisterForm";

const RegisterPage = () => {
    const [password, setPassword] = useState<String>("");

    return(<div className="w-full h-screen items-center flex">
        {!password ? <RegisterForm setPassword={setPassword}/>:
            <div className="w-full justify-center items-center text-3xl">
                <h2>Hasło nowego użytkownika to: <span className="text-red-500 font-bold">{password}</span></h2>
            </div>}
    </div>)
}

export default RegisterPage;