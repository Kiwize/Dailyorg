import { Container, createTheme, CssBaseline, ThemeProvider } from "@mui/material";
import WTAddExerciseToWorkout from "./app/workout-trainer/WTAddExerciseToWorkout";
import WTEditCardioWorkoutExercise from "./app/workout-trainer/WTEditCardioWorkoutExercise";
import WTEditStrengthWorkoutExercise from "./app/workout-trainer/WTEditStrengthWorkoutExercise";
import WTEditWorkoutSession from "./app/workout-trainer/WTEditWorkoutSession";
import WTHomePage from "./app/workout-trainer/WTHomePage";
import DOHomePage from "./app/dailyorg/DOHomePage";
import LoginPage from "./LoginPage";
import MainPage from "./MainPage";
import React, { useState, useEffect } from "react";
import AlertPopup from "./components/AlertPopup";
import { BrowserRouter, Navigate, Route, Routes } from "react-router";
import WTCreateExercise from "./app/workout-trainer/WTCreateExercise";
import Register from "./app/Register";

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
            <Route path="/dohomepage" element={<PrivateRoute><DOHomePage /></PrivateRoute>} />
            <Route path="/edit_workout_strength_exercise" element={<PrivateRoute><WTEditStrengthWorkoutExercise /></PrivateRoute>} />
            <Route path="/edit_workout_cardio_exercise" element={<PrivateRoute><WTEditCardioWorkoutExercise /></PrivateRoute>} />
            <Route path="/add_exercise_to_workout" element={<PrivateRoute><WTAddExerciseToWorkout /></PrivateRoute>} />
            <Route path="/edit_workout" element={<PrivateRoute><WTEditWorkoutSession /></PrivateRoute>} />
            <Route path="create_exercise" element={<PrivateRoute><WTCreateExercise/></PrivateRoute>}></Route>
            <Route path="/wthomepage" element={<PrivateRoute><WTHomePage /></PrivateRoute>} />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/" element={<PrivateRoute><MainPage /></PrivateRoute>} />           
          </Routes>
        </BrowserRouter>
      </ThemeProvider>
    </Container>
  );
}

export default App
