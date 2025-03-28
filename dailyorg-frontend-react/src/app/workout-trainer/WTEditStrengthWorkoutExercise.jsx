import { useLocation } from "react-router";
import Header from "../../Header";
import { Box, Button, Collapse, Divider, IconButton, Input, List, ListItem, ListItemButton, ListItemText, Typography } from "@mui/material";
import {PlusIcon, RepeatIcon, TrashIcon } from "lucide-react";
import { useState, useEffect } from "react";
import useAlert from "../../hooks/useAlert";
import { AccessTime, Done, FitnessCenter} from "@mui/icons-material";

const API_URL = import.meta.env.VITE_API_URL;

export default function WTEditStrengthWorkoutExercise() {
    const location = useLocation();
    const { setAlert } = useAlert();

    const workoutRecordID = location.state.workoutRecordID;

    // Track open forms & their input values separately
    const [formStates, setFormStates] = useState({});
    const [exerciseData, setExerciseData] = useState(null); // Store fetched data manually

    //Data for the add serie form
    const [newRepNumber, setNewRepNumber] = useState(0);
    const [newRepWeight, setNewRepWeight] = useState(0);
    const [newRepRestTime, setNewRepRestTime] = useState(0);

    // Fetch initial data
    const fetchData = async () => {
        try {
            const response = await fetch(`${API_URL}/api/record/get_strength_record`, {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({ strengthRecordID: workoutRecordID })
            });

            if (!response.ok) throw new Error("Failed to fetch data");

            const result = await response.json();
            setExerciseData(result); // Update state with new data
        } catch (error) {
            setAlert("Error loading data...", "error");
        }
    };

    useEffect(() => {
        fetchData(); // Fetch data on component mount
    }, []);

    const collapseAll = () => {
        setFormStates((prevState) => {
            let newState = {};
            for (let key in prevState) {
                newState[key] = { ...prevState[key], open: false }; // Close all items
            }
            return newState;
        });
    };

    const handleClick = (id) => {
        collapseAll();
        setFormStates((prevState) => ({
            ...prevState,
            [id]: {
                ...prevState[id],
                open: !prevState[id]?.open || false,
            },
        }));
    };

    const handleInputChange = (id, field, value) => {
        setFormStates((prevState) => ({
            ...prevState,
            [id]: {
                ...prevState[id], // Keep previous values
                [field]: value,   // Update only the changed field
            },
        }));
    };

    const handleAddSerie = async (e) => {
        e.preventDefault();

        try {
            const response = await fetch(`${API_URL}/api/record/add_strength_serie`, {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({
                    strengthRecordID: workoutRecordID,
                    series: [
                        {
                            repNumber: newRepNumber || 0,
                            repWeight: newRepWeight || 0,
                            restTimeInMins: newRepRestTime || 0,
                        }
                    ]
                }),
            });

            if (!response.ok) throw new Error("Failed to update record");

            setAlert("Strength training updated!", "success");
            setNewRepNumber();
            setNewRepWeight();
            setNewRepRestTime();
            await fetchData();
        } catch (err) {
            setAlert("An error occurred...", "error");
        }
    }

    const handleDeleteSerie = async (workoutSerieID) => {

        try {
            const response = await fetch(`${API_URL}:8080/api/record/delete_strength_serie`, {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({
                    workoutSerieID
                }),
            });

            if (!response.ok) throw new Error("Failed to delete record");

            setAlert("Strength training deleted !", "success");
            await fetchData();
        } catch (err) {
            setAlert("An error occurred...", "error");
        }
    }

    const handleSubmit = async (e, workoutSerieID) => {
        e.preventDefault();

        const { repNumber, repWeight, restTimeInMins } = formStates[workoutSerieID] || {};

        try {
            const response = await fetch(`${API_URL}:8080/api/record/update_strength_serie`, {
                method: "POST",
                headers: { "Content-Type": "application/json", Authorization: "Bearer " + localStorage.getItem("token") },
                body: JSON.stringify({
                    workoutSerieID,
                    repNumber: repNumber || 0,
                    repWeight: repWeight || 0,
                    restTimeInMins: restTimeInMins || 0,
                }),
            });

            if (!response.ok) throw new Error("Failed to update record");

            setAlert("Strength training updated!", "success");
            await fetchData(); // 🔥 Auto-refresh the data
        } catch (err) {
            setAlert("An error occurred...", "error");
        }
    };

    return (
        <div>
            <Header />
            <h1>Edit {exerciseData ? exerciseData.exerciseId.exerciseName : ""}</h1>
            <div>
                {
                    exerciseData ? (
                        <List>
                            {exerciseData.workoutSeries.map(serie => (
                                <div key={serie.workoutSerieID}>
                                    <ListItem >
                                        <ListItemButton sx={{borderRadius: 5}} onClick={() => handleClick(serie.workoutSerieID)}>
                                            <ListItemText>
                                            <Typography variant="p" sx={{ display: "flex", alignItems: "center", justifyContent: 'space-between', width: {sm: 300}}}><RepeatIcon/> {serie.repNumber} <FitnessCenter/> {serie.repWeight} KG <AccessTime/> {serie.restTimeInMins} mins</Typography> 
                                            </ListItemText>
                                        </ListItemButton>
                                        <IconButton variant="outlined" color="error" onClick={() => handleDeleteSerie(serie.workoutSerieID)}>
                                            <TrashIcon />
                                        </IconButton>
                                    </ListItem>
                                    <Collapse in={formStates[serie.workoutSerieID]?.open || false}>
                                        <form onSubmit={(e) => handleSubmit(e, serie.workoutSerieID)}>
                                            <RepeatIcon/>
                                            <Input sx={{mx: 2, width: {xs: 45, sm: 80}}}
                                                type="text"
                                                value={formStates[serie.workoutSerieID]?.repNumber || ""}
                                                onChange={(e) => handleInputChange(serie.workoutSerieID, "repNumber", e.target.value)}
                                            />
                                            <FitnessCenter/>
                                            <Input sx={{mx: 2, width: {xs: 45, sm: 80}}}
                                                type="text"
                                                value={formStates[serie.workoutSerieID]?.repWeight || ""}
                                                onChange={(e) => handleInputChange(serie.workoutSerieID, "repWeight", e.target.value)}
                                            />
                                            <AccessTime/>
                                            <Input sx={{mx: 2, width: {xs: 45, sm: 80}}}
                                                type="text"
                                                value={formStates[serie.workoutSerieID]?.restTimeInMins || ""}
                                                onChange={(e) => handleInputChange(serie.workoutSerieID, "restTimeInMins", e.target.value)}
                                            />
                                            <IconButton type="submit" color="success"><Done/></IconButton>
                                        </form>
                                    </Collapse>
                                </div>
                            ))}
                        </List>
                    ) : "Loading..."

                }
                <div>
                    <Divider sx={{my: 2}}/>
                    <Typography sx={{textAlign: "center"}} variant="h5">New serie</Typography>
                    <form onSubmit={handleAddSerie} >
                        <Box sx={{ display: "flex", flexDirection: "column", px: "20%" }}>
                            <Input
                                sx={{ mb: 3 }}
                                type="text"
                                placeholder="Repetitions"
                                value={newRepNumber || ""}
                                onChange={(e) => setNewRepNumber(e.target.value)}
                                required
                            />
                            <Input
                                sx={{ mb: 3 }}
                                type="text"
                                placeholder="Weight in KG"
                                value={newRepWeight || ""}
                                onChange={(e) => setNewRepWeight(e.target.value)}
                                required
                            />
                            <Input
                                sx={{ mb: 3 }}
                                type="text"
                                placeholder="Rest time in minutes"
                                value={newRepRestTime || ""}
                                onChange={(e) => setNewRepRestTime(e.target.value)}
                                required
                            />
                        </Box>
                        <Box sx={{ textAlign: 'center' }}>
                            <Button sx={{ my: 3 }} type="submit" color="success" variant="contained" size="large" startIcon={<PlusIcon />}>Add</Button>
                        </Box>
                    </form>
                </div>
            </div>
        </div>
    );
}
