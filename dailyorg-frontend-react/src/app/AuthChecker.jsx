import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import callApi from '../hooks/api';

function AuthChecker() {
  const navigate = useNavigate();

  useEffect(() => {
    if (window.location.pathname != '/login' && window.location.pathname != '/register') {
      const checkAuth = async () => {
        try {
          const res = await callApi('GET', 'auth/status', {}, {}, false, false);
          if (res.status === 401) {
            navigate('/login');
          }
        } catch (err) {
          console.error('Error checking auth status:', err);
          navigate('/login');
        }
      };

      // Check immediately
      checkAuth();
      const interval = setInterval(checkAuth, 120_000);

      return () => clearInterval(interval);
    }
  }, [navigate]);

  useEffect(() => {
    const handleVisibilityChange = () => {
      if (!document.hidden) {
        callApi('GET', 'auth/status', {}, {}, false, false).then((res) => {
          if (res.status === 401) {
            navigate('/login');
          }
        });
      }
    };

    document.addEventListener('visibilitychange', handleVisibilityChange);
    return () => document.removeEventListener('visibilitychange', handleVisibilityChange);
  }, [navigate]);

  return null; // This component doesn’t render anything
}

export default AuthChecker;
