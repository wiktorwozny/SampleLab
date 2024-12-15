import NoPermitionPage from "../pages/NoPermitionPage";

export const AdminRoute = ({children}:any):any => {
    return (localStorage.getItem('role') === 'ADMIN' ? <>{children}</>:<NoPermitionPage/>)
}

export const WorkerRoute = ({children}:any):any => {
    return (localStorage.getItem('role') !== 'INTERN' ? <>{children}</>:<NoPermitionPage/>)
}