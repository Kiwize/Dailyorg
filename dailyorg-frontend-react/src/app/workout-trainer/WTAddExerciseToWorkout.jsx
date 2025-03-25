import { Link, useLocation} from "react-router";
import Header from "../../Header";
import WTExerciseTabs from "./WTExerciseTabs";

function WTAddExerciseToWorkout() {
    const location = useLocation();

    return(
        <div>
            <Header></Header>
            <Link to="/edit_workout" state={{workoutid: location.state.workoutid}}>Back</Link>
            <WTExerciseTabs workoutSessionId={location.state.workoutid}></WTExerciseTabs>
        </div>
    );
}

export default WTAddExerciseToWorkout