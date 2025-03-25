package fr.nexa.dailyorg_java.service;

import java.util.List;

import fr.nexa.dailyorg_java.model.CardioExercise;
import fr.nexa.dailyorg_java.model.Exercise;
import fr.nexa.dailyorg_java.model.StrengthExercise;
import fr.nexa.dailyorg_java.model.WorkoutRecord;

public interface IWorkoutRecordService {

	void addStrengthExerciseToWorkout(Long workoutSessionID, StrengthExercise strengthExercise);
	void addCardioExerciseToWorkout(Long workoutSessionID, CardioExercise cardioExercise, int timeSpentInSec, int caloriesBurnt, int intensity);
	void removeExerciseFromWorkout(Long workoutSessionID, Long exerciseID);
	
	boolean isExerciseAlreadyAdded(Long workoutSessionID, Long exerciseToCheck);
	
	List<Exercise> getExercisesByWorkoutSession(Long workoutSessionId);
	
	List<WorkoutRecord> getRecordsByWorkoutSession(Long workoutSessionId);
}
