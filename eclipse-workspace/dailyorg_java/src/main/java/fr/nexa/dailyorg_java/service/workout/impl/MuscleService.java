package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.Muscle;
import fr.nexa.dailyorg_java.repository.workout.IMuscleRepository;
import fr.nexa.dailyorg_java.service.workout.IMuscleService;

@Service
public class MuscleService implements IMuscleService{
	
	@Autowired
	private IMuscleRepository muscleRepository;

	@Override
	public Muscle findByName(String muscleName) {
		return muscleRepository.findByName(muscleName).get();
	}
	
	@Override
	public List<Muscle> findAll() {
		return muscleRepository.findAll();
	}
}
