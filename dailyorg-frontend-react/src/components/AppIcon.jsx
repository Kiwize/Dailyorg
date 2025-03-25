import { useNavigate } from "react-router-dom";
import "../style/AppIcon.css";

function AppIcon({ name, link }) {
    const navigate = useNavigate();

    const appClickHandler = () => {
        if (link) {
            navigate(link);
        } else {
            console.error("No link provided");
        }
    };

    return (
        <div className="appicon" onClick={appClickHandler}>
            <p>{name}</p>
        </div>
    );
}

export default AppIcon;
