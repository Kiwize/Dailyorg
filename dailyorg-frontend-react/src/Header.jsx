import { Box, Button, Link, List, ListItem } from '@mui/material';
import './style/Header.css';
import { useState } from 'react';

const handleLogout = () => {
    localStorage.removeItem("token");
    localStorage.removeItem("username");
    navigate("/login");
};

function Header() {
    const [username, setUsername] = useState(localStorage.getItem("username"));
    
    return(
        <header style={{paddingTop: "15px"}}>
            <nav style={{display: 'flex'}}>
                <List>
                    <ListItem>
                        <Link href="/">Home</Link>
                    </ListItem>
                </List>
                <Box sx={{mx: 'auto'}}/>
                {username && <Button onClick={handleLogout}>Logout</Button>}
            </nav>
        </header>
    );
}

export default Header