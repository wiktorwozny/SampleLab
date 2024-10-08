import LoginForm from "./LoginForm"
import { useEffect, useState } from "react"
const CheckIsLogin = ({ children , isToken, setIsToken}:any):any => {

    const handleStorage = () => {
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