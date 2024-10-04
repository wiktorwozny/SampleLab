import LoginForm from "./LoginForm"
import { useEffect, useState } from "react"
const CheckIsLogin = ({ children }:any):any => {
    const [isToken, setIsToken] = useState<boolean>(false);

    const handleStorage = () => {
        console.log('cos')
        if(localStorage.getItem('token')){
          setIsToken(true);
        } else {
          setIsToken(false);
        }
    }

    useEffect(() => {
        handleStorage();
        window.addEventListener('change', handleStorage)
        return () => window.removeEventListener('change', handleStorage)
      }, [])

    return (isToken ? <>{children}</>:<LoginForm/>)
}

export default CheckIsLogin;