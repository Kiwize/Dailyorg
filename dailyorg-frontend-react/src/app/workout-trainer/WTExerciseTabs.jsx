import { PlusIcon } from "lucide-react";
import useApi from "../../hooks/useApi";
import { Box, Button, Tab, Tabs } from "@mui/material";
import PropTypes from "prop-types";
import React from "react";
import {useNavigate } from "react-router";

export default function WTExerciseTabs({ workoutSessionId }) {
    const email = localStorage.getItem("username");
    const navigate = useNavigate();

    const { fetchData, data, loading, error } = useApi(
        "PUT",
        "workout/add_exercise_to_workout",
        {},
        { Authorization: "Bearer " + localStorage.getItem("token") }
    );

    function addExercise(email, workoutSessionID, exerciseID, exerciseType, cardioTimeSpentInMins = 0, cardioIntensity = 0, cardioCaloriesBurnt = 0) {
        fetchData({
            email: email,
            workout_session_id: workoutSessionID,
            exercise_id: exerciseID,
            exercise_type: exerciseType,
            time_spent_in_mins: cardioTimeSpentInMins,
            intensity: cardioIntensity,
            calories_burnt: cardioCaloriesBurnt
        });

        navigate("/edit_workout", {
            state: { workoutid: workoutSessionID }
        });
    };

    const { data: cardioExercises, loading: cardioExercisesLoading, error: cardioExercisesError } = useApi(
        "POST",
        "exercise/get_all_exercises",
        { "type": "cardio" },
        { Authorization: "Bearer " + localStorage.getItem("token") }
    );

    const { data: strengthExercises, loading: strengthExercisesLoading, error: strengthExercisesError } = useApi(
        "POST",
        "exercise/get_all_exercises",
        { "type": "strength" },
        { Authorization: "Bearer " + localStorage.getItem("token") }
    );

    //##### MUI TABS #####

    function CustomTabPanel(props) {
        const { children, value, index, ...other } = props;

        return (
            <div
                role="tabpanel"
                hidden={value !== index}
                id={`simple-tabpanel-${index}`}
                aria-labelledby={`simple-tab-${index}`}
                {...other}
            >
                {value === index && <Box sx={{ p: 3 }}>{children}</Box>}
            </div>
        );
    }

    CustomTabPanel.propTypes = {
        children: PropTypes.node,
        index: PropTypes.number.isRequired,
        value: PropTypes.number.isRequired,
    };

    function a11yProps(index) {
        return {
            id: `simple-tab-${index}`,
            'aria-controls': `simple-tabpanel-${index}`,
        };
    }

    const [value, setValue] = React.useState(0);

    const handleChange = (event, newValue) => {
        setValue(newValue);
    };

    return (
        <div className="w-full">
            <div className="flex space-x-4 border-b">
                <Tabs value={value} onChange={handleChange}>
                    <Tab label="Cardio" {...a11yProps(0)}></Tab>
                    <Tab label="Strength" {...a11yProps(1)}></Tab>
                </Tabs>
                <CustomTabPanel value={value} index={0}>
                    {
                        cardioExercises ?
                            <div>
                                {cardioExercises.map(cardioExercise => (
                                    <div key={cardioExercise.id} onClick={() => addExercise(email, workoutSessionId, cardioExercise.id, "cardio")}>{cardioExercise.exerciseName}</div>
                                ))}
                            </div> : cardioExercisesLoading
                    }
                </CustomTabPanel>
                <CustomTabPanel value={value} index={1}>
                    {
                        strengthExercises ?
                            <div>
                                {strengthExercises.map(strengthExercise => (
                                    <div key={strengthExercise.id} onClick={() => addExercise(email, workoutSessionId, strengthExercise.id, "strength")}>{strengthExercise.exerciseName}</div>
                                ))}
                            </div> : strengthExercisesLoading
                    }
                </CustomTabPanel>

            </div>
            <Box sx={{textAlign: 'center'}}>
                <Button variant="contained" color="success" startIcon={<PlusIcon/>} onClick={() => navigate("/create_exercise")}>Create exercise</Button>
            </Box>
        </div>
    );
}
