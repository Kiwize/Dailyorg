import { Box, Button, Input, Paper, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import PasswordStrengthTester from './PasswordStrengthTester';
import callApi from '../hooks/api';

export default function Register() {
  const navigate = useNavigate();

  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [error, setError] = useState('');

  const [passwordStrength, setPasswordStrength] = useState(0);

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');

    const data = {
      firstName,
      lastName,
      email,
      password,
      confirmPassword,
    };

    if (password !== confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    if (passwordStrength < 1) {
      setError('Password is too weak');
      return;
    }

    try {
      const result = await callApi('POST', 'register', {
        firstName: data.firstName,
        lastName: data.lastName,
        email: data.email,
        password: data.password,
        confirmPassword: data.confirmPassword,
      }, {}, false);

      localStorage.setItem('token', result.token);
      localStorage.setItem('username', data.email);
      navigate('/');
    } catch (error) {
      console.error('Error during registration:', error);
    }
  };

  const handlePasswordStrengthUpdate = (strength) => {
    setPasswordStrength(strength);
  };

  return (
    <Box sx={{ mx: { xs: '10%', md: '35%' }, pt: 8, textAlign: 'center' }}>
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" sx={{ textAlign: 'center', marginBottom: '10px' }}>
          Register
        </Typography>
        <form onSubmit={handleRegister}>
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Input sx={{ mb: 3 }} type="text" placeholder="First Name" onChange={(e) => setFirstName(e.target.value)} required />
            <Input sx={{ mb: 3 }} type="text" placeholder="Last Name" onChange={(e) => setLastName(e.target.value)} required />
            <Input sx={{ mb: 3 }} type="text" placeholder="Email" onChange={(e) => setEmail(e.target.value)} required />
            <Input sx={{ mb: 3 }} type="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} required />
            <Input sx={{ mb: 3 }} type="password" placeholder="Confirm Password" onChange={(e) => setConfirmPassword(e.target.value)} required />
            <PasswordStrengthTester password={password} updateCallback={handlePasswordStrengthUpdate} />
          </Box>
          <Button type="submit" variant="contained" size="large">
            Register
          </Button>
          <div
            style={{
              color: 'red',
              marginTop: '10px',
              padding: '10px',
              backgroundColor: 'rgba(255, 0, 0, 0.1)',
              borderRadius: '5px',
              display: error !== '' ? 'block' : 'none',
            }}
          >
            {error !== '' && error}
          </div>
        </form>
      </Paper>
      <Button variant="text" onClick={() => navigate('/login')} sx={{ mt: 2 }}>
        Already have an account? Login
      </Button>
    </Box>
  );
}
