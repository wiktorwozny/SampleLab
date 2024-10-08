import NoPermitionPage from "../pages/NoPermitionPage";

const PrivateRoute = ({children}:any):any => {
    return (localStorage.getItem('role') === 'ADMIN' ? <>{children}</>:<NoPermitionPage/>)
}

export default PrivateRoute;