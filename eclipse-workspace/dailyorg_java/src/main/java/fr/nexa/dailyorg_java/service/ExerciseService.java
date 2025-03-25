package fr.nexa.dailyorg_java.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.Exercise;
import fr.nexa.dailyorg_java.repository.IExerciseRepository;

@Service
public class ExerciseService implements IExerciseService{

	@Autowired
	private IExerciseRepository exerciseRepository;
	
	@Override
	public void addExercise(Exercise exercise) throws Exception {
		exerciseRepository.save(exercise);
	}
	
	@Override
	public Exercise getExerciseById(Long exerciseId) throws Exception {
		return exerciseRepository.getReferenceById(exerciseId);
	}
	
	@Override
	public List<Exercise> getAllExercises() throws Exception {
		return exerciseRepository.findAll();
	}
}
