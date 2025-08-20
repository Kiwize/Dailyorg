package fr.nexa.dailyorg.service.workout.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.workout.Exercise;
import fr.nexa.dailyorg.repository.workout.IExerciseRepository;
import fr.nexa.dailyorg.service.workout.IExerciseService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ExerciseService implements IExerciseService {

	private final IExerciseRepository exerciseRepository;

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
