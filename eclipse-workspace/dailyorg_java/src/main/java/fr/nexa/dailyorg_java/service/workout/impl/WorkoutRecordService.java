package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.CardioExercise;
import fr.nexa.dailyorg_java.model.workout.CardioRecord;
import fr.nexa.dailyorg_java.model.workout.Exercise;
import fr.nexa.dailyorg_java.model.workout.StrengthExercise;
import fr.nexa.dailyorg_java.model.workout.StrengthRecord;
import fr.nexa.dailyorg_java.model.workout.WorkoutRecord;
import fr.nexa.dailyorg_java.model.workout.WorkoutSession;
import fr.nexa.dailyorg_java.repository.workout.IWorkoutRecordRepository;
import fr.nexa.dailyorg_java.repository.workout.IWorkoutRepository;
import fr.nexa.dailyorg_java.service.workout.IWorkoutRecordService;

@Service
public class WorkoutRecordService implements IWorkoutRecordService {

	@Autowired
	private IWorkoutRecordRepository workoutRecordRepository;

	@Autowired
	private IWorkoutRepository workoutSessionRepository;

	@Override
	public List<Exercise> getExercisesByWorkoutSession(Long workoutSessionId) {
		return workoutRecordRepository.findExercisesByWorkoutSession(workoutSessionId);
	}
	
	@Override
	public List<WorkoutRecord> getRecordsByWorkoutSession(Long workoutSessionId) {
		return workoutRecordRepository.findByWorkoutSession(workoutSessionId);
	}
	
	@Override
	public boolean isExerciseAlreadyAdded(Long workoutSessionID, Long exerciseToCheckID) {
		List<Exercise> exercises = getExercisesByWorkoutSession(workoutSessionID);
		
		for(Exercise exercise : exercises) {
			if(exercise.getId() == exerciseToCheckID) {
				return true;
			}
		}
		
		return false;
	}

	@Override
	public void addStrengthExerciseToWorkout(Long workoutSessionID, StrengthExercise strengthExercise) {
		WorkoutSession workoutSession = workoutSessionRepository.getReferenceById(workoutSessionID);

		workoutRecordRepository.save(StrengthRecord.builder().workoutSession(workoutSession).exerciseId(strengthExercise).build());
	}

	@Override
	public void addCardioExerciseToWorkout(Long workoutSessionID, CardioExercise cardioExercise, int timeSpentInMins, int caloriesBurnt, int intensity) {
		WorkoutSession workoutSession = workoutSessionRepository.getReferenceById(workoutSessionID);

		workoutRecordRepository.save(CardioRecord.builder().workoutSession(workoutSession).recordTimeSpentInMins(timeSpentInMins).recordCaloriesBurnt(caloriesBurnt).recordIntensity(intensity).exerciseId(cardioExercise).build());
	}

	@Override
	public void removeExerciseFromWorkout(Long workoutSessionID, Long exerciseID) {
		workoutRecordRepository.deleteByWorkoutSessionAndExercise(workoutSessionID, exerciseID);
	}
}
