package fr.nexa.dailyorg_java.service.workout;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.workout.WorkoutSerie;

public interface IWorkoutSerieService {
	
	WorkoutSerie addWorkoutSerie(WorkoutSerie workoutSerie) throws Exception;
	WorkoutSerie updateWorkoutSerie(WorkoutSerie workoutSerie) throws Exception;
	void deleteWorkoutSerie(WorkoutSerie workoutSerie) throws Exception;
	Optional<WorkoutSerie> getWorkoutSerie(Long workoutSerieID) throws Exception;
	
}
