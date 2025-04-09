package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.CardioExercise;
import fr.nexa.dailyorg_java.repository.workout.ICardioExerciseRepository;
import fr.nexa.dailyorg_java.service.workout.ICardioExerciseService;

@Service
public class CardioExerciseService implements ICardioExerciseService{
	
	@Autowired
	private ICardioExerciseRepository cardioExerciseRepository;
	
	@Override
	public void addCardioExercise(CardioExercise cardioExercise) throws Exception {
		cardioExerciseRepository.save(cardioExercise);
	}
	
	@Override
	public boolean isCardioExercise(Long exerciseID) throws Exception {
		return cardioExerciseRepository.findById(exerciseID).isPresent();
	}
	
	@Override
	public CardioExercise getCardioExerciseByID(Long exerciseId) throws Exception {
		return cardioExerciseRepository.getReferenceById(exerciseId);
	}
	
	@Override
	public List<CardioExercise> getAllCardioExercises() throws Exception {
		return cardioExerciseRepository.findAll();
	}

}
