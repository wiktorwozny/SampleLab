import { useContext, useEffect, useState } from "react";
import { getUsersData } from "../helpers/userApi";
import { User } from "../utils/types";
import { checkResponse } from "../utils/checkResponse";
import { Dropdown } from "react-bootstrap";
import { Modal } from "react-bootstrap";
import ConfirmPopup from "./ui/ConfirmPopup";
import { deleteUserByEmail } from "../helpers/userApi";
import { AlertContext } from "../contexts/AlertsContext";

const UserList = () => {
    const [userList, setUserList] = useState<User[]>([])
    const [isChangePasswordModal, setIsChangePasswordModal] = useState<boolean>(false);
    const [userToDelete, setUserToDelete] = useState<String|null>(null);
    const {setAlertDetails} = useContext(AlertContext)
    const deleteUserFunction = async()=>{
        if(userToDelete!==null){
            try{
                let response = await deleteUserByEmail(userToDelete)
                if(response.status === 200){
                    setAlertDetails({isAlert: true, message: "Udało się usunąć użytkownika", type: "success"})
                }
                response = await getUsersData();
                if(response.status === 200){
                    setUserList(response.data);
                }
            }catch(err){
                checkResponse(err)
                console.log(err);
            }
        }
    }

    useEffect(()=>{
        const getUsers = async () => {
            try{
                let response = await getUsersData();
                if(response.status===200){
                    setUserList(response.data)
                }
            }catch(err){
                checkResponse(err);
                console.log(err)
            }
        }
        getUsers();
    }, [])


    return(<div className="flex items-center justify-center">
                <table className="table table-hover table-bordered">
                    <thead>
                    <tr>
                        <th className="!bg-gray-300"/>
                        <th scope={"col"} className="!bg-gray-300">Email</th>
                        <th scope={"col"} className="!bg-gray-300">Imie i nazwisko</th>
                        <th scope={"col"} className="!bg-gray-300">Rola</th>
                    </tr>
                    </thead>
                    <tbody>
                        {userList.length&&userList.map((user,index) => (
                            <tr
                                key={index}
                            >
                                <td>
                                    <Dropdown>
                                        <Dropdown.Toggle
                                            className="bg-sky-500 border-sky-500 hover:bg-sky-600 hover:border-sky-600"
                                            id="dropdown-basic">
                                            ☰
                                        </Dropdown.Toggle>

                                        <Dropdown.Menu>
                                            <Dropdown.Item onClick={()=>setIsChangePasswordModal(true)}>Zmień hasło</Dropdown.Item>
                                            <Dropdown.Item onClick={()=>setUserToDelete(user.email)}>Usuń użytkowanika</Dropdown.Item>
                                        </Dropdown.Menu>
                                    </Dropdown>
                                </td>
                                <td>{user.email}</td>
                                <td>{user.name}</td>
                                <td>{user.role === "WORKER"? "Pracownik":"Admin"}</td>
                            </tr>
                        ))}
                    {/* {samples.map(sample => (
                        <tr
                            key={sample.id} onClick={() => navigate(`/sample/${sample.id}`)}
                        >
                            <td
                                style={{padding: 5, textAlign: 'center', width: 35, height: 35}}
                                onClick={(e) => e.stopPropagation()}>
                                <input
                                    type="checkbox"
                                    checked={isSampleSelected(sample.id)}
                                    onChange={() => handleCheckboxChange(sample.id)}
                                    onClick={(e) => e.stopPropagation()}
                                    style={{width: '80%', height: '80%'}}
                                />
                            </td>
                            <td>{sample.id}</td>
                            <td>{sample.code}</td>
                            <td>{sample.group}</td>
                            <td>{sample.assortment}</td>
                            <td>{sample.clientName}</td>
                            <td>{sample.admissionDate.toString()}</td>
                            <td className={(sample.progressStatus === ProgressStateEnum.DONE ? '!bg-green-100' : '!bg-red-100')}>{progressEnumDesc.get(sample.progressStatus)}</td>
                        </tr>))} */}
                    </tbody>
                </table>
                <Modal show={isChangePasswordModal} onHide={()=>{setIsChangePasswordModal(false)}}>

                </Modal>
                <ConfirmPopup
                    onConfirm={deleteUserFunction}
                    show={userToDelete!==null}
                    handleClose={() => setUserToDelete(null)}
                    message="Czy na pewno chcesz usunąć użytkownika?"
                />
    </div>)
}

export default UserList;