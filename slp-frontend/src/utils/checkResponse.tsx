export const checkResponse = (err: any, setAlertDetails: any = null) => {
    if(err.response.status === 403){
        localStorage.removeItem('token');
        localStorage.removeItem('role');
        window.dispatchEvent(new Event('change'));
        // setAlertDetails({isAlert: true, message: "Sesja się zakończyła zaloguj się ponownie", type: "fail"})
    }
}