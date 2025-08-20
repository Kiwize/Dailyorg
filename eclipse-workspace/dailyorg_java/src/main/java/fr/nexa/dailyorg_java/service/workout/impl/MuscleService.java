package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.Muscle;
import fr.nexa.dailyorg_java.repository.workout.IMuscleRepository;
import fr.nexa.dailyorg_java.service.workout.IMuscleService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class MuscleService implements IMuscleService{
	
	private final IMuscleRepository muscleRepository;

	@Override
	public Optional<Muscle> findByName(String muscleName) {
		return muscleRepository.findByName(muscleName);
	}
	
	@Override
	public List<Muscle> findAll() {
		return muscleRepository.findAll();
	}
}
