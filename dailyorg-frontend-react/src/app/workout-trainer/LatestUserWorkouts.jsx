import WTWorkoutCard from "./WTWorkoutCard";
import { Box } from "@mui/material";
import { useEffect, useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;

function LatestUserWorkouts({ refresh }) {
    const email = localStorage.getItem("username");

    const [workouts, setWorkouts] = useState([]);

    useEffect(() => {
        async function fetchWorkouts() {
            const response = await fetch(`${API_URL}/api/workout/get_user_workout`, {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({ email: email })
            });

            if(response.ok) {
                setWorkouts(await response.json());
            }
            
        }

        fetchWorkouts();
    }, [refresh]);

    return (
        <div>
            {workouts ? 
            <Box>
                {workouts.map(workout => (
                    <WTWorkoutCard id={workout.idWorkoutSession} notes={workout.notes} date={workout.workoutDate}/>
                ))}
            </Box>
            : "Loading..."}
        </div>
    );
}

export default LatestUserWorkouts;
