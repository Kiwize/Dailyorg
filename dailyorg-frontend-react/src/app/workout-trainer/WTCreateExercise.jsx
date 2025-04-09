import { Box, Button, Typography } from "@mui/material";
import Header from "../../Header";
import { useEffect, useState } from "react";
import { PlusIcon } from "lucide-react";
import WTMuscleSelector from "./WTMuscleSelector";
import useApi from "../../hooks/useApi";
import useAlert from "../../hooks/useAlert";

const API_URL = import.meta.env.VITE_API_URL;

export default function WTCreateExercise() {
    const { setAlert } = useAlert();

    const [exerciseType, setType] = useState("cardio");
    const [exerciseName, setExerciseName] = useState("");
    const [exerciseImage, setexerciseImage] = useState("NULL");
    const [muscles, setMuscles] = useState({});

    const handleSubmit = async (e) => {
        e.preventDefault();

        if(exerciseType === "cardio") {
            const response = await fetch(`${API_URL}/api/exercise/add_cardio_exercise`, {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({ exerciseName: exerciseName, exerciseImage: exerciseImage})
            });

            if (!response.ok) {
                throw new Error("Invalid email or password");
            }

            setAlert(`Cardio exercise ${exerciseName} added !`, "success");
        } else if(exerciseType === "strength") {

            setAlert(`Strength exercise ${exerciseName} added !`, "success");
        } else {
            setAlert("Unknown exercise type...", "error");
        }
    }

    const {data, loading, error} = useApi(
        "POST",
        "muscles/get_all_muscles",
        {},
        { Authorization: "Bearer " + localStorage.getItem("token") }
    )

    useEffect(() => {
        if(data) {
            setMuscles(data);
        }       
    }, [data]);
    
    return(
        <Box>
            <Header/>
            <Typography variant="h5" sx={{textAlign: 'center', mt: 2}}>Create exercise</Typography>
            <form onSubmit={handleSubmit} style={{display: 'flex', flexDirection: 'column'}}>
                <Box>
                    <label><input type="radio" name="exercise_type" value={exerciseType} onChange={() => setType("cardio")} defaultChecked></input>Cardio</label>
                    <label><input type="radio" name="exercise_type" value={exerciseType} onChange={() => setType("strength")} ></input>Strength</label>
                </Box>
                <label>Name : <input type="text" onChange={(e) => {setExerciseName(e.target.value)}} required></input></label>
                <label>Image (Optionnal) : <input type="file" onChange={(e) => {setexerciseImage(e.target.value)}}></input></label>     
                {
                    exerciseType == "cardio" ? "" :
                    <Box>
                        <WTMuscleSelector muscles={muscles}/>
                    </Box>
                }

                <Button sx={{mx: 'auto'}} type="submit" variant="contained" startIcon={<PlusIcon/>}>Create</Button>
            </form>
        </Box>
    );
}