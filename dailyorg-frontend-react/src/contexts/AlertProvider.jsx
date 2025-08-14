import { createContext, useState } from 'react';
import AlertPopup from '../components/AlertPopup';

const ALERT_TIME = 4000;

const AlertContext = createContext({
  setAlert: () => {},
});

export const AlertProvider = ({ children }) => {
  //List of current alerts
  const [alerts, setAlerts] = useState([]);

  const setAlert = (text, type) => {
    // Add the new alert to the list
    const id = Date.now() + Math.random();
    const newAlert = { id, text, type };

    setAlerts((prevAlerts) => [...prevAlerts, newAlert]);

    setTimeout(() => {
      setAlerts((prevAlerts) => prevAlerts.filter((alert) => alert.id !== id));
    }, ALERT_TIME);
  };

  return (
    //Display the list of alerts
    <AlertContext.Provider value={{ setAlert }}>
      {children}

      {alerts.map((alert) => (
        <div
          key={alert.id}
          style={{
            position: 'fixed',
            right: 15,
            top: 15 + alerts.indexOf(alert) * 70, // Stack alerts vertically
            zIndex: 1000,
          }}
        >
          <AlertPopup text={alert.text} type={alert.type} />
        </div>
      ))}
    </AlertContext.Provider>
  );
};

export default AlertContext;
