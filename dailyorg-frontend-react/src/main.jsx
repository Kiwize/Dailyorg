import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import './index.css'
import App from './App.jsx'
import { AlertProvider } from './contexts/AuthContext.jsx'

createRoot(document.getElementById('root')).render(
  <AlertProvider>
    <StrictMode>
      <App />
    </StrictMode>
  </AlertProvider>
)
