import LoginForm from "./LoginForm"

const CheckIsLogin = ({ children }:any):any => {
    return (localStorage.getItem('token') ? <>{children}</>:<LoginForm/>)
}

export default CheckIsLogin;