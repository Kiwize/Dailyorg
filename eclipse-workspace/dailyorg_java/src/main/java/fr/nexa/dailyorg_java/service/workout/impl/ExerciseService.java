package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.Exercise;
import fr.nexa.dailyorg_java.repository.workout.IExerciseRepository;
import fr.nexa.dailyorg_java.service.workout.IExerciseService;

@Service
public class ExerciseService implements IExerciseService {

	@Autowired
	private IExerciseRepository exerciseRepository;

	@Override
	public void addExercise(Exercise exercise) throws Exception {
		exerciseRepository.save(exercise);
	}

	@Override
	public Optional<Exercise> getExerciseById(Long exerciseId) throws Exception {
		return exerciseRepository.findById(exerciseId);
	}

	@Override
	public List<Exercise> getAllExercises() throws Exception {
		return exerciseRepository.findAll();
	}
}
