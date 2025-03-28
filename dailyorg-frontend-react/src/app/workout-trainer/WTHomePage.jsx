import LatestUserWorkouts from "./LatestUserWorkouts";
import Header from "../../Header";
import { Box, Button, Typography } from "@mui/material";
import { PlusIcon } from "lucide-react";
import { useState } from "react";

const API_URL = import.meta.env.VITE_API_URL;

const handleClick = async (e, setRefresh) => {
    e.preventDefault();
    const email = localStorage.getItem("username");

    try {
        const response = await fetch(`${API_URL}/api/workout/create_workout`, {
            method: "POST",
            headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
            body: JSON.stringify({
                email: email
            }),
        });

        if(!response.ok) {
            throw new Error("An error occured while adding the workout...");
        }

        setRefresh(prev => !prev);
    } catch (err) {
        setAlert("An error occurred...", "error");
    }
}

function WTHomePage() {
    const [refresh, setRefresh] = useState(false);

    return (
        <Box>
            <Header />
            <Typography variant="h4" sx={{ my: 2 }}>Workout Trainer</Typography>
            <LatestUserWorkouts refresh={refresh} />
            <Box sx={{ textAlign: 'center', pt: 3 }}>
                <Button color="success" variant="contained" startIcon={<PlusIcon/>} onClick={(e) => handleClick(e, setRefresh)}>New workout session</Button>
            </Box>
        </Box>
    );
}

export default WTHomePage