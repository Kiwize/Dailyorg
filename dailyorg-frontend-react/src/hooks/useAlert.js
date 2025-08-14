import { useContext } from 'react';
import AlertContext from '../contexts/AlertProvider';

const useAlert = () => useContext(AlertContext);

export default useAlert;
