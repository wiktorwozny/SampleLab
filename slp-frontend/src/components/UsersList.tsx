import React, {useContext, useEffect, useState} from "react";
import {deleteUserByEmail, getUsersData} from "../helpers/userApi";
import {User} from "../utils/types";
import {checkResponse} from "../utils/checkResponse";
import {Dropdown, Modal} from "react-bootstrap";
import ConfirmPopup from "./ui/ConfirmPopup";
import {AlertContext} from "../contexts/AlertsContext";
import ChangePasswordFormAdminForm from "./ChangePasswordForAdminForm";
import { RoleEnumDesc } from "../utils/enums";

const UserList = () => {
    const [userList, setUserList] = useState<User[]>([])
    const [userToChangePassword, setUserToChangePassword] = useState<String | null>(null);
    const [userToDelete, setUserToDelete] = useState<String | null>(null);
    const {setAlertDetails} = useContext(AlertContext)
    const deleteUserFunction = async () => {
        if (userToDelete !== null) {
            try {
                let response = await deleteUserByEmail(userToDelete)
                if (response.status === 200) {
                    setAlertDetails({isAlert: true, message: "Udało się usunąć użytkownika", type: "success"})
                }
                response = await getUsersData();
                if (response.status === 200) {
                    setUserList(response.data);
                }
            } catch (err) {
                checkResponse(err)
                console.log(err);
            }
        }
    }

    useEffect(() => {
        const getUsers = async () => {
            try {
                let response = await getUsersData();
                if (response.status === 200) {
                    setUserList(response.data)
                }
            } catch (err) {
                checkResponse(err);
                console.log(err)
            }
        }
        getUsers();
    }, [])


    return (<div className="flex items-center justify-center">
        <table className="table table-hover table-bordered">
            <thead>
            <tr>
                <th className="w-20">Akcja</th>
                <th scope={"col"} className="text-left">Adres e-mail</th>
                <th scope={"col"} className="text-left">Imię i nazwisko</th>
                <th scope={"col"} className="text-left">Rola</th>
            </tr>
            </thead>
            <tbody>
            {userList.length && userList.map((user, index) => (
                <tr
                    key={index}
                    className="text-left"
                >
                    <td>
                        <Dropdown>
                            <Dropdown.Toggle
                                className="bg-sky-500 border-sky-500 hover:bg-sky-600 hover:border-sky-600"
                                id="dropdown-basic">
                                ☰
                            </Dropdown.Toggle>

                            <Dropdown.Menu>
                                <Dropdown.Item onClick={() => setUserToChangePassword(user.email)}>Zmień
                                    hasło</Dropdown.Item>
                                <Dropdown.Item onClick={() => setUserToDelete(user.email)}>Usuń
                                    użytkowanika</Dropdown.Item>
                            </Dropdown.Menu>
                        </Dropdown>
                    </td>
                    <td>{user.email}</td>
                    <td>{user.name}</td>
                    <td>{RoleEnumDesc.find(role => role.value === user.role)?.label}</td>
                </tr>
            ))}
            </tbody>
        </table>
        <Modal show={userToChangePassword !== null} onHide={() => {
            setUserToChangePassword(null)
        }}>
            <ChangePasswordFormAdminForm email={userToChangePassword} closeHandler={setUserToChangePassword}/>
        </Modal>
        <ConfirmPopup
            onConfirm={deleteUserFunction}
            show={userToDelete !== null}
            handleClose={() => setUserToDelete(null)}
            message="Czy na pewno chcesz usunąć użytkownika?"
        />
    </div>)
}

export default UserList;