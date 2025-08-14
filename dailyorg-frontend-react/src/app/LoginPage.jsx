import React, { useState } from "react";
import { Box, Button, Input, Paper, Typography } from "@mui/material";
import { useNavigate } from "react-router";
import useAlert from "../hooks/useAlert";
import { sha256 } from "js-sha256";
const API_URL = import.meta.env.VITE_API_URL;

function LoginPage() {
    const navigate = useNavigate();
    const alert = useAlert();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();

        setError("");

        try {
            const response = await fetch(`${API_URL}/api/login`, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                credentials: 'include',
                body: JSON.stringify({ email, password })
            });

            if (!response.ok) {
                alert.setAlert("Invalid email or password", "error");
                throw new Error("Invalid email or password");
            }

            await response.json();
            localStorage.setItem("username", email);

            navigate("/");
        } catch (err) {
        }
    };

    return (
        <Box sx={{ mx: { xs: "10%", md: "35%" }, pt: 8, textAlign: 'center', height: '100vh', display: 'flex', flexDirection: 'column', justifyContent: 'center' }}>
            <Paper elevation={3} sx={{p: 4}}>
                <Typography variant="h4" sx={{ textAlign: 'center' }}>Login</Typography>
                <form onSubmit={handleLogin}>
                    <Box sx={{ display: 'flex', flexDirection: 'column' }}>
                        <Input sx={{ mb: 3 }} type="text" placeholder="Email" value={email} onChange={(e) => setEmail(e.target.value)} required />
                        <Input sx={{ mb: 3 }} type="password" placeholder="Password" value={password} onChange={(e) => setPassword(e.target.value)} required />
                    </Box>
                    <Button type="submit" variant="contained" size="large">Login</Button>
                </form>
                {error && <p style={{ color: "red" }}>{error}</p>}
            </Paper>
            <Button
                variant="text"
                onClick={() => navigate("/register")}
                sx={{ mt: 2 }}
            >
                Don't have an account? Register
            </Button>
            {
                error && <Typography variant="body2" color="error" sx={{ mt: 2 }}>
                    {`${API_URL}/api/login`}
                </Typography>
            }
        </Box>
    );
}

export default LoginPage