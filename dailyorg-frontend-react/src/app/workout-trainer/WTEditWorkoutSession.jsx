import { useLocation, useNavigate } from "react-router";
import Header from "../../Header";
import useApi from "../../hooks/useApi";
import { Box, Button, Divider, List, ListItem, ListItemButton, ListItemText } from "@mui/material";
import { PlusIcon } from "lucide-react";

function WTEditWorkoutSession() {
    const location = useLocation();
    const navigate = useNavigate();
    const workoutid = location.state.workoutid;

    const loggedUserEmail = localStorage.getItem("username");

    //Fetch all exercises attached to the selected workout.
    const { data, loading, error } = useApi(
        "POST",
        "workout/get_workout_exercises",
        {
            "email": loggedUserEmail,
            "workout_session_id": workoutid
        },
        { Authorization: "Bearer " + localStorage.getItem("token") }
    );

    return (
        <div>
            <Header />
            <h1>Editing Workout</h1>
            <List>
                {
                    data ?
                        <Box>
                            <Box>
                                {
                                    data.strength.map(exercise => (
                                        <ListItem key={exercise.id} disablePadding onClick={() => navigate("/edit_workout_strength_exercise", { state: { workoutID: workoutid, workoutRecordID: exercise.id } })}>
                                            <ListItemButton>
                                                <ListItemText primary={exercise.exerciseId.exerciseName} />
                                            </ListItemButton>
                                        </ListItem>
                                    ))
                                }
                            </Box>
                            <Divider />
                            <Box>
                                {
                                    data.cardio.map(exercise => (
                                        <ListItem key={exercise.id} disablePadding onClick={() => navigate("/edit_workout_cardio_exercise", { state: { workoutID: workoutid, workoutRecordID: exercise.id, isEdited: true } })}>
                                            <ListItemButton>
                                                <ListItemText primary={exercise.exerciseId.exerciseName} />
                                            </ListItemButton>
                                        </ListItem>
                                    ))
                                }
                            </Box>
                        </Box>
                        : "Loading..."
                }


            </List>
            <Box sx={{ textAlign: 'center' }}>
                <Button variant="contained" color="success" startIcon={<PlusIcon />} onClick={() => navigate("/add_exercise_to_workout", { state: { workoutid: location.state.workoutid } })}>
                    New exercise
                </Button>
            </Box>
        </div>
    );


}

export default WTEditWorkoutSession