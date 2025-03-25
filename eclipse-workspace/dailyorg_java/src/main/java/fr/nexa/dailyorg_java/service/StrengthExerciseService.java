package fr.nexa.dailyorg_java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.StrengthExercise;
import fr.nexa.dailyorg_java.repository.IMuscleRepository;
import fr.nexa.dailyorg_java.repository.IStrengthExerciseRepository;

@Service
public class StrengthExerciseService implements IStrengthExerciseService{
	
	@Autowired
	private IStrengthExerciseRepository strengthExerciseRepository;
	
	@Autowired
	private IMuscleRepository muscleRepository;
	
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
