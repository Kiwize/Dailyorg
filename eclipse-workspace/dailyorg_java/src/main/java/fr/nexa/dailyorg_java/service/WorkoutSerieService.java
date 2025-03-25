package fr.nexa.dailyorg_java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.WorkoutSerie;
import fr.nexa.dailyorg_java.repository.IWorkoutSerieRepository;

@Service
public class WorkoutSerieService implements IWorkoutSerieService{

	@Autowired
	private IWorkoutSerieRepository workoutSerieRepository;
	
	@Override
	public Optional<WorkoutSerie> getWorkoutSerie(Long workoutSerieID) throws Exception {
		return workoutSerieRepository.findById(workoutSerieID);
	}
	
	@Override
	public WorkoutSerie addWorkoutSerie(WorkoutSerie workoutSerie) throws Exception {
		return workoutSerieRepository.save(workoutSerie);
	}
	
	@Override
	public WorkoutSerie updateWorkoutSerie(WorkoutSerie workoutSerie) throws Exception {
		return workoutSerieRepository.save(workoutSerie);
	}
	    
	@Override
	public void deleteWorkoutSerie(Long workoutSerieID) throws Exception {
		 workoutSerieRepository.deleteById(workoutSerieID);
	}
	
}
