
import { useNavigate } from 'react-router';
import '../../style/WorkoutCard.css';
import { Card, CardActionArea, CardContent, IconButton, Typography } from "@mui/material";
import { TrashIcon } from "lucide-react";
import useAlert from '../../hooks/useAlert';

const API_URL = import.meta.env.VITE_API_URL;

function WTWorkoutCard({ id, notes, date }) {
    const navigate = useNavigate();
    const { setAlert }  = useAlert();

    const handleDeleteWorkout = async (workoutID) => {
        try {
            const response = await fetch(`${API_URL}/api/workout/delete_workout`, {
                method: "DELETE",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({
                    workout_session_id: workoutID
                }),
            });

            if (!response.ok) throw new Error("Failed to delete workout session !");

            setAlert("Strength training deleted !", "success");
        } catch (err) {
            console.error(err);
            setAlert("An error occurred...", "error");
        }
    }

    return (
        <Card sx={{ borderRadius: 3, display: 'flex', mb: 1 }}>
            <CardActionArea onClick={() => navigate("/edit_workout", { state: { workoutid: id } })}>
                <CardContent>
                    <Typography gutterBottom><strong>Date:</strong> {date}</Typography>
                    <Typography><strong>Notes:</strong> {notes}</Typography>
                </CardContent>
            </CardActionArea>
            <IconButton sx={{my: 'auto'}} variant='contained' color='error' onClick={() => handleDeleteWorkout(id)}><TrashIcon/></IconButton>
        </Card>
    );
}

export default WTWorkoutCard