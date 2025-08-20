package fr.nexa.dailyorg_java.service.workout;

import java.util.List;
import java.util.Optional;

import fr.nexa.dailyorg_java.model.workout.Muscle;

public interface IMuscleService {

	Optional<Muscle> findByName(String muscleName);
	List<Muscle> findAll();
	
}
