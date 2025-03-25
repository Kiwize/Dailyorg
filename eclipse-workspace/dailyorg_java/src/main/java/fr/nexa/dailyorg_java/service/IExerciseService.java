package fr.nexa.dailyorg_java.service;

import java.util.List;

import fr.nexa.dailyorg_java.model.Exercise;

public interface IExerciseService {

	void addExercise(Exercise exercise) throws Exception;
	
	Exercise getExerciseById(Long exerciseId) throws Exception;
	List<Exercise> getAllExercises() throws Exception;
	
	
}
