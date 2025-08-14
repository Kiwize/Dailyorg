import { useEffect, useState } from 'react';
import callApi from '../hooks/api';
import BackendUnreachable from '../components/BackendUnreachable';
import LoadingScreen from '../components/LoadingScreen';

export default function BackendChecker({ children }) {
  const [backendUp, setBackendUp] = useState(null);

  useEffect(() => {
    const checkBackend = async () => {
      try {
        const res = await callApi('GET', 'health', {}, {});
        setBackendUp(true);
      } catch (err) {
        console.error('Backend unreachable:', err.message);
        setBackendUp(false);
      }
    };

    checkBackend();
  }, []);

  if (backendUp === null) return (
    <LoadingScreen />
  );

  if (backendUp === false)
    return (
      <BackendUnreachable />    
    );

  return children;
}
