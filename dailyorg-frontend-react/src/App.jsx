import { Container, createTheme, CssBaseline, ThemeProvider } from '@mui/material';
import WTAddExerciseToWorkout from './app/workout-trainer/WTAddExerciseToWorkout';
import WTEditCardioWorkoutExercise from './app/workout-trainer/WTEditCardioWorkoutExercise';
import WTEditStrengthWorkoutExercise from './app/workout-trainer/WTEditStrengthWorkoutExercise';
import WTEditWorkoutSession from './app/workout-trainer/WTEditWorkoutSession';
import WTHomePage from './app/workout-trainer/WTHomePage';
import DOHomePage from './app/dailyorg/DOHomePage';
import LoginPage from './app/LoginPage';
import MainPage from './MainPage';
import { BrowserRouter, Route, Routes } from 'react-router';
import WTCreateExercise from './app/workout-trainer/WTCreateExercise';
import Register from './app/Register';
import AuthChecker from './app/AuthChecker';
import { useToolbar } from './contexts/ToolbarProvider';
import { useEffect, useState } from 'react';
import ProfilePage from './app/ProfilePage';
import NotFoundView from './components/NotFoundView';

function PrivateRoute({ children }) {
  return children;
}

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
  },
});

function App() {


  return (
    <Container>
      <ThemeProvider theme={darkTheme}>
        <CssBaseline />
        <BrowserRouter>
          <AuthChecker />
          <Routes>
            <Route 
            path='/profilepage'
            element={
              <PrivateRoute>
                <ProfilePage/>
              </PrivateRoute>
            }
            />
            <Route
              path="/dohomepage"
              element={
                <PrivateRoute>
                  <DOHomePage />
                </PrivateRoute>
              }
            />
            <Route
              path="/edit_workout_strength_exercise"
              element={
                <PrivateRoute>
                  <WTEditStrengthWorkoutExercise />
                </PrivateRoute>
              }
            />
            <Route
              path="/edit_workout_cardio_exercise"
              element={
                <PrivateRoute>
                  <WTEditCardioWorkoutExercise />
                </PrivateRoute>
              }
            />
            <Route
              path="/add_exercise_to_workout"
              element={
                <PrivateRoute>
                  <WTAddExerciseToWorkout />
                </PrivateRoute>
              }
            />
            <Route
              path="/edit_workout"
              element={
                <PrivateRoute>
                  <WTEditWorkoutSession />
                </PrivateRoute>
              }
            />
            <Route
              path="create_exercise"
              element={
                <PrivateRoute>
                  <WTCreateExercise />
                </PrivateRoute>
              }
            ></Route>
            <Route
              path="/wthomepage"
              element={
                <PrivateRoute>
                  <WTHomePage />
                </PrivateRoute>
              }
            />
            <Route path="/register" element={<Register />} />
            <Route path="/login" element={<LoginPage />} />
            <Route
              path="/"
              element={
                <PrivateRoute>
                  <MainPage />
                </PrivateRoute>
              }
            />
            <Route path="*" element={<NotFoundView />} />
          </Routes>
        </BrowserRouter>
      </ThemeProvider>
    </Container>
  );
}

export default App;
