package fr.nexa.dailyorg_java.service;

import fr.nexa.dailyorg_java.model.Muscle;

public interface IMuscleService {

	Muscle findByName(String muscleName);
	
}
