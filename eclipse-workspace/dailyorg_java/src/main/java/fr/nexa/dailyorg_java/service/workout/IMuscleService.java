package fr.nexa.dailyorg_java.service.workout;

import java.util.List;

import fr.nexa.dailyorg_java.model.workout.Muscle;

public interface IMuscleService {

	Muscle findByName(String muscleName);
	
	List<Muscle> findAll();
	
}
