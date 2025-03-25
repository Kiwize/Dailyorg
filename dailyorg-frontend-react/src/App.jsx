import { Container, createTheme, CssBaseline, ThemeProvider } from "@mui/material";
import WTAddExerciseToWorkout from "./app/workout-trainer/WTAddExerciseToWorkout";
import WTAddWorkout from "./app/workout-trainer/WTAddWorkout";
import WTEditCardioWorkoutExercise from "./app/workout-trainer/WTEditCardioWorkoutExercise";
import WTEditStrengthWorkoutExercise from "./app/workout-trainer/WTEditStrengthWorkoutExercise";
import WTEditWorkoutSession from "./app/workout-trainer/WTEditWorkoutSession";
import WTHomePage from "./app/workout-trainer/WTHomePage";
import LoginPage from "./LoginPage";
import MainPage from "./MainPage";
import React, { useState, useEffect } from "react";
import AlertPopup from "./components/AlertPopup";
import { BrowserRouter, Navigate, Route, Routes } from "react-router";

function PrivateRoute({ children }) {
  return localStorage.getItem("token") ? children : <Navigate to="/login" />;
}

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

function App() {
  return (
    <Container sx={{height: "100vh" }}>
      <ThemeProvider theme={darkTheme}>
        <CssBaseline />
        <BrowserRouter>
          <AlertPopup />
          <Routes>
            <Route path="/edit_workout_strength_exercise" element={<WTEditStrengthWorkoutExercise />} />
            <Route path="/edit_workout_cardio_exercise" element={<WTEditCardioWorkoutExercise />} />
            <Route path="/add_exercise_to_workout" element={<WTAddExerciseToWorkout />} />
            <Route path="/edit_workout" element={<WTEditWorkoutSession />} />
            <Route path="/add_workout" element={<WTAddWorkout />} />
            <Route path="/wthomepage" element={<WTHomePage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/" element={<PrivateRoute><MainPage /></PrivateRoute>} />
          </Routes>
        </BrowserRouter>
      </ThemeProvider>
    </Container>
  );
}

export default App
