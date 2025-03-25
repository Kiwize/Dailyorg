import { useLocation, useNavigate } from "react-router";
import Header from "../../Header";
import { useState } from "react";
import { Box, Button, Input, Typography } from "@mui/material";
import useAlert from "../../hooks/useAlert";
import useApi from "../../hooks/useApi";
import { useEffect } from "react";
import { AccessTime, LocalFireDepartment } from "@mui/icons-material";
import { BoltIcon } from "lucide-react";

export default function WTEditCardioWorkoutExercise() {
    const navigate = useNavigate();
    const location = useLocation();
    const { setAlert } = useAlert();

    const workoutRecordID = location.state.workoutRecordID;

    const isEdited = location.state.isEdited ? location.state.isEdited : false;

    const [timeSpent, setTimeSpent] = useState(0);
    const [intensity, setIntensity] = useState(0);
    const [calories, setCalories] = useState(0);

    const [error, setError] = useState("");

    if (isEdited) {
        const { data, loading, error } = useApi(
            "POST",
            "record/get_cardio_record",
            {
                "cardioRecordID": workoutRecordID
            },
            { Authorization: "Bearer " + localStorage.getItem("token") }
        );

        useEffect(() => {
            if (data) {
                setTimeSpent(data.recordTimeSpentInMins);
                setIntensity(data.recordIntensity);
                setCalories(data.recordCaloriesBurnt);
            }
        }, [data]);
    }

    const handleLogin = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const response = await fetch("http://192.168.1.142:8080/api/record/update_cardio_record", {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({ cardioRecordID: workoutRecordID, calories_burnt: calories, intensity, time_spent_in_mins: timeSpent })
            });

            if (!response.ok) throw new Error("Failed to update record");

            setAlert("Cardio training updated!", "success");
            navigate("/edit_workout", { state: { workoutid: location.state.workoutID } });

        } catch (err) {
            console.log(err);
            setAlert("An error occurred...", "error");
        }
    };

    return (
        <Box>
            <Header></Header>
            <Typography variant="h4" sx={{ textAlign: 'center', py: 3 }}>Cardio exercise edit</Typography>
            <form onSubmit={handleLogin}>
                <Box sx={{ display: 'flex', flexDirection: "column"}}>
                    <Box sx={{ display: 'flex', pt: 2, mx: 'auto'}}>
                        <AccessTime />
                        <Input sx={{ ml: 2 }} type="text" placeholder="Time spent (Minutes)" value={timeSpent} onChange={(e) => setTimeSpent(e.target.value)} required />
                    </Box>
                    <Box sx={{ display: 'flex', pt: 2, mx: 'auto' }}>
                        <BoltIcon/> 
                        <Input sx={{ ml: 2 }} type="text" placeholder="Intensity" value={intensity} onChange={(e) => setIntensity(e.target.value)} required />
                    </Box>
                    <Box sx={{ display: 'flex', pt: 2, mx: 'auto' }}>
                        <LocalFireDepartment/>
                        <Input sx={{ ml: 2 }} type="text" placeholder="Calories burnt" value={calories} onChange={(e) => setCalories(e.target.value)} required />
                    </Box>
                </Box>
                <Box sx={{ textAlign: 'center', pt: 3 }}>
                    <Button type="submit" color="success" variant="contained">Update</Button>
                </Box>
            </form>
        </Box>
    );
}