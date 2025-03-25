import React, { useState } from "react";
import { Box, Button, Input, Paper, Typography } from "@mui/material";
import { useNavigate } from "react-router";

const API_URL = "http://localhost:8080/api/login"; // Replace with your actual backend URL

function LoginPage() {
    const navigate = useNavigate();
    const [email, setEmail] = useState("");
    const [password, setPassword] = useState("");
    const [error, setError] = useState("");

    const handleLogin = async (e) => {
        e.preventDefault();
        setError("");

        try {
            const response = await fetch(API_URL, {
                method: "POST",
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ email, password })
            });

            if (!response.ok) {
                throw new Error("Invalid email or password");
            }

            const data = await response.json();
            localStorage.setItem("token", data.token);
            localStorage.setItem("username", email);
            navigate("/");
        } catch (err) {
            setError(err.message);
        }
    };

    return (
        <Box sx={{ mx: { xs: "10%", md: "35%" }, pt: 8, textAlign: 'center' }}>
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
        </Box>
    );
}

export default LoginPage