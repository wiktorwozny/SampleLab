import {useContext, useEffect, useState} from "react";
import {Indication} from "../../../utils/types";
import {AlertContext} from "../../../contexts/AlertsContext";
import {deleteIndication, getAllIndications} from "../../../helpers/indicationApi";

const IndicationDict = () => {

    const [indicationList, setIndicationList] = useState<Indication[]>([]);
    const {setAlertDetails} = useContext(AlertContext);
    const [openModal, setOpenModal] = useState(false);
    const [selectedItem, setSelectedItem] = useState<Indication | null>(null);
    const [isViewMode, setIsViewMode] = useState(false);
    const [isAddMode, setIsAddMode] = useState(false);
    const [isEditMode, setIsEditMode] = useState(false);

    const handleView = (item: Indication) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(true);
        setIsAddMode(false);
        setIsEditMode(false);
    };

    const handleEdit = (item: Indication) => {
        setSelectedItem(copyObject(item));
        setOpenModal(true);
        setIsViewMode(false);
        setIsAddMode(false);
        setIsEditMode(true);
    };

    const handleAdd = () => {
        setSelectedItem(null);  // No item selected since we're adding a new client
        setOpenModal(true);
        setIsViewMode(false);
        setIsAddMode(true);
        setIsEditMode(false);
    };


    const handleDelete = async (item: Indication) => {
        try {
            let response = await deleteIndication(item?.id)
            console.log(response)
            if (response.status === 201 || response.status === 200) {
                setAlertDetails({isAlert: true, message: "Usunięto definicję", type: "success"})
                getIndications();
                handleClose();
            }
        } catch (err) {
            console.log(err)
            setAlertDetails({isAlert: true, message: "Wystąpił bład spróbuj ponownie później", type: "error"})
        }
    };

    const handleClose = () => {
        setOpenModal(false);
    }

    const copyObject = (item: Indication): Indication => {
        return JSON.parse(JSON.stringify(item));
    };

    const getIndications = () => {
        getAllIndications().then((res) => {
            if (res.status === 200) {
                setIndicationList(res.data);
            }
        })
    };

    useEffect(() => {
        getIndications();
    }, []);

    return (<div></div>)
}

export default IndicationDict