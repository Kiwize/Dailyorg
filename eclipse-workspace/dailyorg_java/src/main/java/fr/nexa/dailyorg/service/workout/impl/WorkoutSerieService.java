package fr.nexa.dailyorg.service.workout.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.workout.WorkoutSerie;
import fr.nexa.dailyorg.repository.workout.IWorkoutSerieRepository;
import fr.nexa.dailyorg.service.workout.IWorkoutSerieService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WorkoutSerieService implements IWorkoutSerieService {

	private final IWorkoutSerieRepository workoutSerieRepository;

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
	public void deleteWorkoutSerie(WorkoutSerie workoutSerie) throws Exception {
		workoutSerieRepository.delete(workoutSerie);
	}

}
