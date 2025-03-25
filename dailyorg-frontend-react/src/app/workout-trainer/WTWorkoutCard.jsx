
import { useNavigate } from 'react-router';
import '../../style/WorkoutCard.css';
import { Card, CardActionArea, CardContent, Typography } from "@mui/material";

function WTWorkoutCard({ id, notes, date }) {
    const navigate = useNavigate();

    return (
        <Card sx={{ borderRadius: 3 }} onClick={() => navigate("/edit_workout", { state: { workoutid: id } })}>
            <CardActionArea>
                <CardContent>
                    <Typography gutterBottom><strong>Date:</strong> {date}</Typography>
                    <Typography><strong>Notes:</strong> {notes}</Typography>
                </CardContent>
            </CardActionArea>
        </Card>
    );
}

export default WTWorkoutCard