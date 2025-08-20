package fr.nexa.dailyorg.service.workout.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.workout.StrengthExercise;
import fr.nexa.dailyorg.repository.workout.IStrengthExerciseRepository;
import fr.nexa.dailyorg.service.workout.IStrengthExerciseService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StrengthExerciseService implements IStrengthExerciseService{
	
	private final IStrengthExerciseRepository strengthExerciseRepository;
	
	@Override
	public void addStrengthExercise(StrengthExercise strengthExercise) throws Exception {
		strengthExerciseRepository.save(strengthExercise);
	}
	
	@Override
	public boolean isStrengthExercise(Long exerciseID) throws Exception {
		return strengthExerciseRepository.findById(exerciseID).isPresent();
	}
	
	@Override
	public List<StrengthExercise> getExercisesByMuscle(Long muscleId) {
        return strengthExerciseRepository.findByMuscleId(muscleId);
    }
	
	@Override
	public List<StrengthExercise> getAllStrengthExercises() throws Exception {
		return strengthExerciseRepository.findAll();
	}
	
	@Override
	public StrengthExercise getStrengthExerciseByID(Long exerciseId) throws Exception {
		return strengthExerciseRepository.getReferenceById(exerciseId);
	}
}
