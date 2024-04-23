import { useEffect, useState } from "react"
import { Div } from "./ui/Div"
import { Sample } from "../utils/types"
import { Button } from "./ui/Button"
import { getAllSamples } from "../helpers/samplingApi"
import { useNavigate } from "react-router-dom"
const SampleList = () => {

    const [samples, setSamples] = useState<Sample []>([])
    const [isLoading, setIsLoading] = useState<boolean>(true)
    const navigate = useNavigate()
    useEffect(()=>{
        const getSamples = async() => {
            try {
                let response = await getAllSamples();
                if(response.status === 200){
                    setSamples(response.data)
                    setIsLoading(false)
                }
            }catch(err) {
                console.log(err);
            }
        }
        getSamples()
    },[])
    return(<div className="RoomListPage flex flex-col items-center h-fit justify-center">
        {!isLoading&&samples.map(sample=>(<Div key={sample.id} className="flex justify-between hover:bg-slate-100 cursor-default" onClick={()=>{navigate(`/sample/${sample.id}`)}}>
            <div>
                <span className="font-bold">Nazwa:&nbsp;</span> 
                {sample.client.name}
            </div>
            <div>
                <span className="font-bold">id: </span> 
                {sample.id}
            </div>
        </Div>))}
        {isLoading&&<div className="text-2xl">Loading...</div>}
        <Button className="mt-2" type="button" onClick={()=>{navigate('/addSample')}}>Dodaj nową próbkę</Button>
    </div>
    )
}

export default SampleList