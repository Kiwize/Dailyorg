import React from "react";
import LoadingScreen from "../components/LoadingScreen";


export const LoadingContext = React.createContext();

export const LoadingProvider = ({ children }) => {
    const [isLoading, setIsLoading] = React.useState(false);

    return (
        <LoadingContext.Provider value={{ isLoading, setIsLoading }}>
            {children}
            {
                isLoading && (
                    <LoadingScreen />
                )
            }
            
        </LoadingContext.Provider>
    );
}

export default LoadingContext;