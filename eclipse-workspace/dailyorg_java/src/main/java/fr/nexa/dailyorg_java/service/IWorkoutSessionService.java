package fr.nexa.dailyorg_java.service;

import java.util.List;

import fr.nexa.dailyorg_java.model.Exercise;
import fr.nexa.dailyorg_java.model.WorkoutSession;

public interface IWorkoutSessionService {

	void addWorkoutSession(WorkoutSession workoutSession) throws Exception;
	void deleteWorkoutSession(WorkoutSession workoutSession) throws Exception;
	WorkoutSession getWorkoutSessionById(Long workoutSessionId) throws Exception;
	void updateWorkoutSession(WorkoutSession workoutSession) throws Exception;
	
	List<WorkoutSession> getWorkoutSessionsByEmail(String appUserEmail) throws Exception;
	
	List<Exercise> getWorkoutSessionExercises(WorkoutSession workoutSession) throws Exception;
	
}
