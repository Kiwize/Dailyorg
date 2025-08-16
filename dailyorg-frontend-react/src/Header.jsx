import { Box, Button, Link, List, ListItem } from '@mui/material';
import { useNavigate } from 'react-router-dom';
import callApi from './hooks/api';
import { useEffect } from 'react';
import { sha256 } from 'js-sha256';
import { useToolbar } from './contexts/ToolbarProvider';

const BASE_URL = import.meta.env.VITE_API_URL;

const handleLogout = async (navigate) => {
  await callApi('POST', 'logout', {}, {}, false, false);

  localStorage.clear();

  navigate('/login');
};

function Header() {
  const navigate = useNavigate();
  const toolBar = useToolbar();
  try {
    var filename = sha256(localStorage.getItem('username')) + '.webp';
  } catch (e) {
    filename = 'user_dark.webp'; // Fallback in case of error
    console.error('Error generating filename:', e);
  }

  useEffect(() => {
    toolBar.setToolbarEnabled(false);
  }, []);

  return (
    //Header sticks to the top of the page
    <div style={{ position: 'sticky', top: 16, zIndex: 1000, display: 'flex', alignItems: 'center', maxHeight: '56px' }}>
      <header style={{ backgroundColor: '#383838bb', borderRadius: '20px', flexGrow: 1 }}>
        <nav style={{ display: 'flex' }}>
          <List>
            <ListItem>
              <Link href="/">Home</Link>
            </ListItem>
          </List>
          <Box sx={{ mx: 'auto' }} />
          {localStorage.getItem('username') && <Button onClick={() => handleLogout(navigate)}>Logout</Button>}
        </nav>
      </header>
      <div style={{ marginLeft: '16px', backgroundColor: '#38383866', borderRadius: '20px', padding: '0px' }}>
        <img
          src={`${BASE_URL}/uploads/profile_pictures/${filename}`}
          alt="Profile Picture"
          style={{ borderRadius: '50%', width: '56px', aspectRatio: '1/1', objectFit: 'cover', padding: '3px' }}
          onClick={() => navigate('/profilepage')}
          onError={(e) => {
            e.target.onerror = null;
            e.target.src = 'images\\user_dark.webp';
          }}
        />
      </div>
    </div>
  );
}

export default Header;
