package fr.nexa.dailyorg.service.workout;

import java.util.List;

import fr.nexa.dailyorg.model.workout.CardioExercise;

public interface ICardioExerciseService {

	void addCardioExercise(CardioExercise cardioExercise) throws Exception;
	
	CardioExercise getCardioExerciseByID(Long exerciseId) throws Exception;
	
	List<CardioExercise> getAllCardioExercises() throws Exception;
	
	boolean isCardioExercise(Long exerciseID) throws Exception;
	
}
