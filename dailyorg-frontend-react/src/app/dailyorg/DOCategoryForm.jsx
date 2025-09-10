import { Box, Button } from "@mui/material";
import React from "react";
import callApi from "../../hooks/api";


export default function DOCategoryForm({ onClose }) {
    

    const [category, setCategory] = React.useState({ name: '', color: '#000000' });

    const [categories, setCategories] = React.useState([]);

    //When component in mounted, load user's categories from backend
    React.useEffect(() => {
        const fetchCategories = async () => {
            const result = await callApi('GET', 'category/get_all_by_user', null, false, false, true);
            setCategories(result);
        };
        fetchCategories();
    }, []);

    return (
        <Box
            sx={{
                position: 'fixed',
                top: 0,
                left: 0,
                zIndex: 1000,
                width: '100%',
                height: '100%',
                backgroundColor: 'rgba(0, 0, 0, 0.5)',
                display: 'flex',
                justifyContent: 'center',
                alignItems: 'center',
            }}
        >
            <Box
                sx={{
                    position: 'fixed',
                    top: '50%',
                    left: '50%',
                    transform: 'translate(-50%, -50%)',
                    minWidth: '300px',
                    minHeight: '200px',
                    backgroundColor: 'background.paper',
                    boxShadow: 24,
                    p: 4,
                    zIndex: 2000,
                }}
            >
                <Button variant="contained" onClick={() => onClose()} sx={{ mb: 2 }}>
                    Close Settings
                </Button>
                <Box>
                    <h2>Task categories</h2>
                    <Box>
                        <label>
                            Category Name:
                            <input type="text" value={category.name} onChange={(e) => setCategory({ ...category, name: e.target.value })} />
                        </label>
                    </Box>
                    <Box>
                        <label>
                            Category Color:
                            <input type="color" value={category.color} onChange={(e) => setCategory({ ...category, color: e.target.value })} />
                        </label>
                    </Box>
                </Box>
            </Box>
        </Box>
    );
}