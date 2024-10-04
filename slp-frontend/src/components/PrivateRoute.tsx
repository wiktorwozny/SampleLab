import NoPermitionPage from "../pages/NoPermitionPage";

const PrivateRoute = ({children}:any):any => {
    return (localStorage.getItem('role') === 'admin' ? <>{children}</>:<NoPermitionPage/>)
}

export default PrivateRoute;