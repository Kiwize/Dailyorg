import React, { useState, useEffect } from "react";
import { useNavigate } from "react-router";
import './style/MainPage.css';
import { Box, CardActionArea, CardContent, CardMedia, Grid2, Paper, Typography } from "@mui/material";
import Header from "./Header";

const apps = [
    {
        name: "Workout Trainer",
        navigateTo: "/wthomepage",
        img: "images\\cards\\workout_trainer.webp",
        imgAlt: "workout weights"
    }
];

function MainPage() {
    const navigate = useNavigate();
    const [username, setUsername] = useState(localStorage.getItem("username"));

    return (
        <div>
            <Header/>
            <Typography variant="h4" sx={{my: 2}}>Hello {username || "Guest"}</Typography>
            <Box>
                <Grid2 container spacing={2}>
                    {
                        apps.map((app) => (
                            <Grid2 key={app.name} size={{ xs: 12, sm: 4 }}>
                                <Paper elevation={3}>
                                    <CardActionArea onClick={() => navigate(app.navigateTo)}>
                                        <CardMedia
                                            component="img"
                                            height="140"
                                            image={app.img}
                                            alt={app.imgAlt}
                                        />
                                        <CardContent>
                                            <Typography gutterBottom variant="h5" component="div">
                                                {app.name}
                                            </Typography>
                                        </CardContent>
                                    </CardActionArea>
                                </Paper>
                            </Grid2>
                        ))
                    }
                </Grid2>
            </Box>

        </div>
    );
}

export default MainPage