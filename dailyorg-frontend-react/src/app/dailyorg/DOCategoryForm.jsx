import { Box, Button, IconButton, Typography } from "@mui/material";
import React from "react";
import callApi from "../../hooks/api";

import RefreshIcon from '@mui/icons-material/Refresh';
import CloseIcon from '@mui/icons-material/Close';
import EditOffIcon from '@mui/icons-material/EditOff';
import useAlert from "../../hooks/useAlert";


export default function DOCategoryForm({ onClose }) {
    const alert = useAlert();

    const [selectedCategoryId, setSelectedCategoryId] = React.useState(null);
    const [category, setCategory] = React.useState({ name: '', color: '#000000' });
    const [categories, setCategories] = React.useState([]);

    //When component in mounted, load user's categories from backend
    const fetchCategories = async () => {
        const result = await callApi('GET', 'category/get_all_by_user', null, false, false, true);
        setCategories(result.content);
    };

    React.useEffect(() => {
        fetchCategories();
    }, []);

    const onAddCategory = async () => {
        if (category.name.trim() === '') {
            alert.setAlert('Category name cannot be empty', 'error');
            return;
        }

        if (selectedCategoryId) {
            // Update existing category
            const result = await callApi('POST', `category/update`, {
                idCategory: selectedCategoryId,
                taskCategoryName: category.name,
                taskCategoryColor: category.color,
            }, false, false, true);
            if (result.content) {
                setCategories((prev) => prev.map((cat) => (cat.id === selectedCategoryId ? result.content : cat)));
                setCategory({ name: '', color: '#000000' });
                setSelectedCategoryId(null);

                alert.setAlert('Category updated successfully', 'success');
            } else {
                alert.setAlert('Error updating category', 'error');
            }
        } else {
            // Create new category
            const result = await callApi('PUT', 'category/create', {
                taskCategoryName: category.name,
                taskCategoryColor: category.color,
            }, false, false, true);
            if (result.content) {
                setCategories((prev) => [...prev, result.content]);
                setCategory({ name: '', color: '#000000' });
                alert.setAlert('Category created successfully', 'success');
            } else {
                alert.setAlert('Error creating category', 'error');
            }
        }

        fetchCategories();
    }

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
                <IconButton variant="contained" onClick={() => onClose()} sx={{ mb: 2, position: 'absolute', top: 8, right: 8 }}>
                    <CloseIcon />
                </IconButton>
                <Box>
                    <Typography variant="h6" sx={{ mb: 2 }}>Manage Categories</Typography>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 1, maxHeight: '300px', overflowY: 'auto', mb: 2 }}>
                        {
                            categories.length > 0 ? (
                                categories.map((cat) => (
                                    <Box key={cat.id} sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                                        <Box sx={{ width: 20, height: 20, backgroundColor: cat.taskCategoryColor, borderRadius: '4px' }}></Box>
                                        <span onClick={() => {
                                            if (cat.organizerUser === null) {
                                                alert.setAlert('You cannot edit this category', 'error');
                                                return;
                                            }
                                            setCategory({ name: cat.taskCategoryName, color: cat.taskCategoryColor });
                                            setSelectedCategoryId(cat.idCategory);
                                        }}
                                        style={{ cursor: cat.organizerUser !== null ? 'pointer' : 'not-allowed', flexGrow: 1, fontWeight: selectedCategoryId === cat.idCategory ? 'bold' : 'normal' }}
                                        >{cat.taskCategoryName}</span>
                                        {
                                            cat.organizerUser !== null && (
                                                <IconButton aria-label="delete" size="small" onClick={async () => {
                                                    const result = await callApi('DELETE', 'category/delete', {
                                                        idCategory: cat.idCategory,
                                                        taskCategoryName: cat.taskCategoryName, taskCategoryColor: cat.taskCategoryColor
                                                    }, false, false, true);
                                                    if (result.status === 200) {
                                                        setCategories((prev) => prev.filter((c) => c.idCategory !== cat.idCategory));
                                                    } else {
                                                        alert.setAlert('Error deleting category', 'error');
                                                    }
                                                }}>
                                                    <CloseIcon fontSize="small" />
                                                </IconButton>
                                            )
                                        }
                                    </Box>
                                ))
                            ) : (
                                <span>No categories found. Create one!</span>
                            )
                        }
                    </Box>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
                        <input
                            type="text"
                            placeholder="Category Name"
                            value={category.name}
                            onChange={(e) => setCategory({ ...category, name: e.target.value })}
                        />
                        <input
                            type="color"
                            value={category.color}
                            onChange={(e) => setCategory({ ...category, color: e.target.value })}
                            style={{ width: '50px', height: '50px', padding: 0, border: 'none', background: 'none' }}
                        />
                    </Box>
                    <Box>
                        {
                            selectedCategoryId && (
                                <IconButton aria-label="cancel edit" onClick={() => {
                                    setSelectedCategoryId(null);
                                    setCategory({ name: '', color: '#000000' });
                                }}>
                                    <EditOffIcon />
                                </IconButton>
                            )
                        }
                        <Button variant="contained" sx={{ mt: 2 }} onClick={onAddCategory}>{selectedCategoryId ? 'Update' : 'Add'} Category</Button>
                    </Box>
                </Box>
            </Box>
        </Box>
    );
}