import LatestUserWorkouts from "./LatestUserWorkouts";
import Header from "../../Header";
import { Box, Button, Typography } from "@mui/material";
import { useNavigate } from "react-router";
import { PlusIcon } from "lucide-react";

function WTHomePage() {
    const navigate = useNavigate();

    return (
        <Box>
            <Header />
            <Typography variant="h4" sx={{ my: 2 }}>Workout Trainer</Typography>
            <LatestUserWorkouts />
            <Box sx={{ textAlign: 'center', pt: 3 }}>
                <Button color="success" variant="contained" startIcon={<PlusIcon/>} onClick={() => navigate("/add_workout")}>New workout session</Button>
            </Box>
        </Box>
    );
}

export default WTHomePage