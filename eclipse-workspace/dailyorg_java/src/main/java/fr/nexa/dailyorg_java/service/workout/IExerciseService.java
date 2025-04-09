package fr.nexa.dailyorg_java.service.workout;

import java.util.List;
import java.util.Optional;

import fr.nexa.dailyorg_java.model.workout.Exercise;

public interface IExerciseService {

	void addExercise(Exercise exercise) throws Exception;

	Optional<Exercise> getExerciseById(Long exerciseId) throws Exception;

	List<Exercise> getAllExercises() throws Exception;

}
