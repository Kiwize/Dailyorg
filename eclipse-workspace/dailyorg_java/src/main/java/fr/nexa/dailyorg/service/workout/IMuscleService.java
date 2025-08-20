package fr.nexa.dailyorg.service.workout;

import java.util.List;
import java.util.Optional;

import fr.nexa.dailyorg.model.workout.Muscle;

public interface IMuscleService {

	Optional<Muscle> findByName(String muscleName);
	List<Muscle> findAll();
	
}
