package fr.nexa.dailyorg_java.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.Muscle;
import fr.nexa.dailyorg_java.repository.IMuscleRepository;

@Service
public class MuscleService implements IMuscleService{
	
	@Autowired
	private IMuscleRepository muscleRepository;

	@Override
	public Muscle findByName(String muscleName) {
		return muscleRepository.findByName(muscleName).get();
	}
}
