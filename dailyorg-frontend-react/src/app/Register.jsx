import { Box, Button, Input, Paper, Typography } from '@mui/material';
import { useState } from 'react';
import { useNavigate } from 'react-router';
import PasswordStrengthTester from './PasswordStrengthTester';
import callApi from '../hooks/api';
import useAlert from '../hooks/useAlert';
import { DatasetRounded } from '@mui/icons-material';

export default function Register() {
  const navigate = useNavigate();
  const alert = useAlert();

  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');

  const [passwordStrength, setPasswordStrength] = useState(0);

  const handleRegister = async (e) => {
    e.preventDefault();

    const data = Object.fromEntries(new FormData(e.target).entries());
    console.log('Form Data:', data);

    if (password !== confirmPassword) {
      alert.setAlert('Passwords do not match', 'error');
      return;
    }

    if (passwordStrength < 1) {
      alert.setAlert('Password is too weak', 'error');
      return;
    }

    try {
      const result = await callApi(
        'POST',
        'register',
        {
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          password: data.password,
          confirmPassword: data.confirmPassword
        },
        {},
        false,
        true
      );

      console.log('Registration result:', result);

      if(result.status !== 200) {
        console.error('Registration error:', result.message);
        alert.setAlert(result.message, 'error');
        return;
      }

      if (result.status === 200) {
        //Upload profile picture if provided
        alert.setAlert('Registration successful', 'success');
        console.log(data.profilePicture);
        if (data.profilePicture) {
          const formData = new FormData();
          formData.append('profilePicture', data.profilePicture);
          formData.append('email', data.email);
          await callApi('POST', 'upload/profile_picture', formData, {}, false, false, true);
        }
      }

      localStorage.setItem('username', data.email);
      navigate('/');
    } catch (error) {
      console.error('Registration error:', error);
      alert.setAlert('Registration failed', 'error');
    }
  };

  const handlePasswordStrengthUpdate = (strength) => {
    setPasswordStrength(strength);
  };

  return (
    <Box
      sx={{
        mx: { xs: '10%', md: '35%' },
        pt: 8,
        textAlign: 'center',
        minHeight: '100vh',
        display: 'flex',
        flexDirection: 'column',
        justifyContent: 'center',
      }}
    >
      <Paper elevation={3} sx={{ p: 4 }}>
        <Typography variant="h4" sx={{ textAlign: 'center', marginBottom: '10px' }}>
          Register
        </Typography>
        <form onSubmit={handleRegister}>
          <Box sx={{ display: 'flex', flexDirection: 'column' }}>
            <Input sx={{ mb: 3 }} type="text" name="firstName" placeholder="First Name" required />
            <Input sx={{ mb: 3 }} type="text" name="lastName" placeholder="Last Name" required />
            <Input sx={{ mb: 3 }} type="text" name="email" placeholder="Email" required />
            <Input sx={{ mb: 3 }} type="password" name="password" placeholder="Password" onChange={(e) => setPassword(e.target.value)} required />
            <Input sx={{ mb: 3 }} type="password" name="confirmPassword" placeholder="Confirm Password" onChange={(e) => setConfirmPassword(e.target.value)} required />
            {/* Upload profile picture */}
            <Input sx={{ mb: 3 }} type="file" accept="image/*" name="profilePicture" />

            <PasswordStrengthTester password={password} updateCallback={handlePasswordStrengthUpdate} />
          </Box>
          <Button type="submit" variant="contained" size="large">
            Register
          </Button>
        </form>
      </Paper>
      <Button variant="text" onClick={() => navigate('/login')} sx={{ mt: 2 }}>
        Already have an account? Login
      </Button>
    </Box>
  );
}
