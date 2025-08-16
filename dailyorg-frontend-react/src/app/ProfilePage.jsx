import { Box, Input, Typography } from '@mui/material';
import { useEffect, useRef, useState } from 'react';
import callApi from '../hooks/api';
import useAlert from '../hooks/useAlert';
import DriveFolderUploadIcon from '@mui/icons-material/DriveFolderUpload';
import BorderColorIcon from '@mui/icons-material/BorderColor';
import useWindowSize from '../hooks/useWindowSize';
import { sha256 } from 'js-sha256';

export default function ProfilePage() {
  const [profilePicture, setProfilePicture] = useState(null);
  const alert = useAlert();

  const { width } = useWindowSize();

  const BASE_URL = import.meta.env.VITE_API_URL;

  try {
    var filename = sha256(localStorage.getItem('username')) + '.webp';
  } catch (e) {
    filename = 'user_dark.webp'; // Fallback in case of error
    console.error('Error generating filename:', e);
  }

  const [editMode, setEditMode] = useState(false);
  const [userFirstName, setUserFirstName] = useState('');
  const [userLastName, setUserLastName] = useState('');
  const [userEmail, setUserEmail] = useState('');

  const fetchUserProfile = async () => {
    try {
      const result = await callApi('GET', 'user/me', {}, {});
      if (result.status === 200) {
        setUserFirstName(result.content.surname);
        setUserLastName(result.content.username);
        setUserEmail(result.content.email);
      }
    } catch (error) {
      console.error('Error fetching user profile:', error);
      alert.setAlert('Failed to fetch user profile', 'error');
    }
  };

  const handleProfileUpdate = async (e) => {
    e.preventDefault();

    if (profilePicture) {
      //Check the file type
      const allowedTypes = ['image/jpeg', 'image/png', 'image/webp'];
      if (!allowedTypes.includes(profilePicture.type)) {
        alert.setAlert('Unsupported file type. Please upload JPG, PNG, or WEBP.', 'error');
        return;
      }

      const formData = new FormData();
      formData.append('profilePicture', profilePicture);
      await callApi('POST', 'upload/profile_picture', formData, {}, false, false, true);
    }

    // Update user profile information
    const data = {
      surname: userFirstName,
      username: userLastName,
      email: userEmail,
    };

    const result = await callApi('POST', 'user/update_user', data, {}, false, true);

    if (result.status === 200) {
      alert.setAlert('Profile updated successfully', 'success');
    } else {
      alert.setAlert('Failed to update profile', 'error');
    }
  };

  // Fetch user profile when component mounts
  useEffect(() => {
    fetchUserProfile();
  }, []);

  return (
    <Box
      sx={{
        minHeight: '100vh',
      }}
    >
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
            width: { xs: '90%', sm: '70%', md: '50%' },
            minHeight: '200px',
            backgroundColor: 'background.paper',
            boxShadow: 24,
            p: 4,
            zIndex: 2000,
            borderRadius: 8,
          }}
        >
          <Typography variant="h4" sx={{ mb: 2, textAlign: 'center' }}>
            Profile Page
          </Typography>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'row',
              borderRadius: 6,
              border: '1px solid #ccc',
              padding: 2,
              marginBottom: 2,
            }}
          >
            <Box sx={{ padding: 2 }}>
              <Typography variant="body1" sx={{ mb: 2 }}>
                Update profile picture
              </Typography>
              <Box sx={{ display: 'flex', flexDirection: width >= 800 ? 'row' : 'column', alignItems: 'center', gap: 2 }}>
                <Box>
                  <Box>
                    <Input
                      type="file"
                      name="profilePicture"
                      accept="image/*"
                      style={{ marginBottom: '16px' }}
                      onChange={(e) => setProfilePicture(e.target.files[0])}
                    />
                  </Box>
                  <Typography variant="body2" color="text.secondary">
                    Supported formats: JPG, PNG, WEBP
                  </Typography>
                </Box>
                <Box>
                  {
                    <img
                      src={
                        `${BASE_URL}/uploads/profile_pictures/${filename}` && !profilePicture
                          ? `${BASE_URL}/uploads/profile_pictures/${filename}`
                          : URL.createObjectURL(profilePicture)
                      }
                      alt="Profile Preview"
                      style={{ width: '200px', height: '200px', borderRadius: '8px', maskRepeat: 'no-repeat', maskSize: 'cover', objectFit: 'cover' }}
                      onError={(e) => {
                        e.target.onerror = null;
                        e.target.src = 'images\\user_dark.webp';
                      }}
                    />
                  }
                </Box>
              </Box>
            </Box>
          </Box>
          <Box
            sx={{
              display: 'flex',
              flexDirection: 'row',
              borderRadius: 6,
              border: '1px solid #ccc',
              padding: 2,
              marginBottom: 2,
            }}
          >
            {/* Surname and name, the infos are displayed as text and the user clicks a pencil icon to enable editing*/}

            <Box sx={{ flexGrow: 1, paddingRight: 2 }}>
              {!editMode && (
                <Box>
                  <Typography variant="body1" sx={{ mb: 1 }}>
                    First Name: {userFirstName}
                  </Typography>
                  <Typography variant="body1" sx={{ mb: 1 }}>
                    Last Name: {userLastName}
                  </Typography>
                  <Typography variant="body1" sx={{ mb: 1 }}>
                    Email:
                    <span id="email"> {userEmail}</span>
                  </Typography>
                </Box>
              )}
              {editMode && (
                <Box>
                  <Input
                    type="text"
                    name="firstName"
                    placeholder="First Name"
                    value={userFirstName}
                    onChange={(e) => setUserFirstName(e.target.value)}
                    sx={{ mb: 1, width: '100%' }}
                  />
                  <Input
                    type="text"
                    name="lastName"
                    placeholder="Last Name"
                    value={userLastName}
                    onChange={(e) => setUserLastName(e.target.value)}
                    sx={{ mb: 1, width: '100%' }}
                  />
                  <Input
                    type="email"
                    name="email"
                    placeholder="Email"
                    value={userEmail}
                    onChange={(e) => setUserEmail(e.target.value)}
                    sx={{ mb: 1, width: '100%' }}
                  />
                </Box>
              )}
            </Box>
            <Box sx={{ display: 'flex', alignItems: 'center' }}>
              <BorderColorIcon
                sx={{ cursor: 'pointer', width: '24px', height: '24px' }}
                onClick={() => {
                  setEditMode(true);
                }}
              />
            </Box>
          </Box>
          <Box sx={{ display: 'flex', justifyContent: 'flex-end' }}>
            <button
              onClick={handleProfileUpdate}
              style={{ marginRight: '8px', padding: '8px 16px', borderRadius: '4px', backgroundColor: '#4caf50', color: '#fff', border: 'none' }}
            >
              Update Profile
            </button>
            <button
              onClick={() => window.history.back()}
              style={{ padding: '8px 16px', borderRadius: '4px', backgroundColor: '#1976d2', color: '#fff', border: 'none' }}
            >
              Go Back
            </button>
          </Box>
        </Box>
      </Box>
    </Box>
  );
}
