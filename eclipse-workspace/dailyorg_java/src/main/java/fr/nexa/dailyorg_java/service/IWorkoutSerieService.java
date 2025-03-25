package fr.nexa.dailyorg_java.service;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.WorkoutSerie;

public interface IWorkoutSerieService {
	
	WorkoutSerie addWorkoutSerie(WorkoutSerie workoutSerie) throws Exception;
	WorkoutSerie updateWorkoutSerie(WorkoutSerie workoutSerie) throws Exception;
	void deleteWorkoutSerie(Long workoutSerieID) throws Exception;
	Optional<WorkoutSerie> getWorkoutSerie(Long workoutSerieID) throws Exception;
	
}
