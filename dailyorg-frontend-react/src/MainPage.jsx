import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router';
import './style/MainPage.css';
import { Box, CardActionArea, CardContent, CardMedia, Grid2, Paper, Typography } from '@mui/material';
import Header from './Header';
import callApi from './hooks/api';

const apps = [
  {
    name: 'Workout Trainer',
    navigateTo: '/wthomepage',
    img: 'images\\cards\\workout_trainer.webp',
    imgAlt: 'workout weights',
  },
  {
    name: 'Daily Organizer',
    navigateTo: '/dohomepage',
    img: 'images\\cards\\daily_organizer.webp',
    imgAlt: 'workout weights',
  },
];

function MainPage() {
  const navigate = useNavigate();
  const [username, setUsername] = useState('Guest');

  useEffect(() => {
    async function fetchData() {
      const response = await callApi('GET', 'user/me', {}, {});
      if (response.status === 200) {
        setUsername(response.content.surname);
      } else {
        console.error('Failed to fetch user data');
      }
    }

    fetchData();
  }, []);

  return (
    <div
      style={{
        minHeight: '100vh',
      }}
    >
      <Header />
      <Typography variant="h4" sx={{ my: 2 }}>
        Hello {username || 'Guest'}
      </Typography>

      <Box>
        <Grid2 container spacing={2}>
          {apps.map((app) => (
            <Grid2 key={app.name} size={{ xs: 12, sm: 4 }}>
              <Paper elevation={3}>
                <CardActionArea onClick={() => navigate(app.navigateTo)}>
                  <CardMedia component="img" height="140" image={app.img} alt={app.imgAlt} />
                  <CardContent>
                    <Typography gutterBottom variant="h5" component="div">
                      {app.name}
                    </Typography>
                  </CardContent>
                </CardActionArea>
              </Paper>
            </Grid2>
          ))}
        </Grid2>
      </Box>
    </div>
  );
}

export default MainPage;
