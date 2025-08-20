package fr.nexa.dailyorg.service.workout.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.workout.CardioExercise;
import fr.nexa.dailyorg.repository.workout.ICardioExerciseRepository;
import fr.nexa.dailyorg.service.workout.ICardioExerciseService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CardioExerciseService implements ICardioExerciseService{
	
	private final ICardioExerciseRepository cardioExerciseRepository;
	
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
