import WTWorkoutCard from "./WTWorkoutCard";
import useApi from "../../hooks/useApi";
import { Box } from "@mui/material";


function LatestUserWorkouts() {
    const email = localStorage.getItem("username");

    const { data, loading, error } = useApi(
        "POST",
        "workout/get_user_workout",
        {"email":email},
        { Authorization: "Bearer " + localStorage.getItem("token") }
    );

    return (
        <div>
            {data ? 
            <Box>
                {data.map(workout => (
                    <WTWorkoutCard id={workout.idWorkoutSession} notes={workout.notes} date={workout.workoutDate}/>
                ))}
            </Box>
            : "Loading..."}
        </div>
    );
}

export default LatestUserWorkouts;
