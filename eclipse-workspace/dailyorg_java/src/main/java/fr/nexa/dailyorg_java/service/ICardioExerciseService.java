package fr.nexa.dailyorg_java.service;

import java.util.List;

import fr.nexa.dailyorg_java.model.CardioExercise;

public interface ICardioExerciseService {

	void addCardioExercise(CardioExercise cardioExercise) throws Exception;
	
	CardioExercise getCardioExerciseByID(Long exerciseId) throws Exception;
	
	List<CardioExercise> getAllCardioExercises() throws Exception;
	
	boolean isCardioExercise(Long exerciseID) throws Exception;
	
}
