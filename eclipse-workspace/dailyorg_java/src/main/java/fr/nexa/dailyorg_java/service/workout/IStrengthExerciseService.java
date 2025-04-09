package fr.nexa.dailyorg_java.service.workout;

import java.util.List;

import fr.nexa.dailyorg_java.model.workout.StrengthExercise;

public interface IStrengthExerciseService {

	void addStrengthExercise(StrengthExercise strengthExercise) throws Exception;
	StrengthExercise getStrengthExerciseByID(Long exerciseId) throws Exception;
	
	List<StrengthExercise> getExercisesByMuscle(Long muscleId) throws Exception;
	
	List<StrengthExercise> getAllStrengthExercises() throws Exception;
	
	boolean isStrengthExercise(Long exerciseID) throws Exception;
}
 