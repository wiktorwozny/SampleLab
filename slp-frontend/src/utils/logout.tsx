export const logout = (setAlertDetails:any = null) => {
    localStorage.removeItem("token");
    localStorage.removeItem('role');
    window.dispatchEvent(new Event('change'));
    setAlertDetails({isAlert: true, message: "Wylogowałeś się pomyślnie", type: "success"})
}