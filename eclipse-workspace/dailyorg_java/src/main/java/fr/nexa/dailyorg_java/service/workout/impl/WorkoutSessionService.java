package fr.nexa.dailyorg_java.service.workout.impl;

import java.lang.reflect.InaccessibleObjectException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.AppUser;
import fr.nexa.dailyorg_java.model.workout.Exercise;
import fr.nexa.dailyorg_java.model.workout.WorkoutSession;
import fr.nexa.dailyorg_java.repository.workout.IAppUserRepository;
import fr.nexa.dailyorg_java.repository.workout.IWorkoutRepository;
import fr.nexa.dailyorg_java.service.workout.IWorkoutSessionService;
import jakarta.transaction.Transactional;

@Service
public class WorkoutSessionService implements IWorkoutSessionService{
	
	@Autowired
	private IWorkoutRepository workoutSessionRepository;
	
	@Autowired
	private IAppUserRepository appUserRepository;

	@Override
	public List<WorkoutSession> getWorkoutSessionsByEmail(String appUserEmail) throws Exception {
		Optional<AppUser> optionalUser = appUserRepository.findByEmail(appUserEmail);
		if(optionalUser.isPresent()) {
			return workoutSessionRepository.findByUserId(optionalUser.get());
		}
		
		throw new InaccessibleObjectException("Invalid or non-existent username...");
	}
	
	@Override
	public List<Exercise> getWorkoutSessionExercises(WorkoutSession workoutSession) throws Exception {
		return null;
	}
	
	@Override
	public Optional<WorkoutSession> getWorkoutSessionById(Long workoutSessionId) throws Exception {
		return workoutSessionRepository.findById(workoutSessionId);
	}
	
	@Override
	public void addWorkoutSession(WorkoutSession workoutSession) throws Exception {
		workoutSessionRepository.save(workoutSession);
	}
	
	@Override
	public void updateWorkoutSession(WorkoutSession workoutSession) throws Exception {
		workoutSessionRepository.save(workoutSession);
	}
	
	@Override
	@Transactional
	public void deleteWorkoutSession(Long workoutSessionID) throws Exception {
		Optional<WorkoutSession> ws = getWorkoutSessionById(workoutSessionID);
		if(!ws.isPresent())
			throw new IllegalStateException("The workout session doesn't exist...");
			
		WorkoutSession workoutSession = ws.get();
		
		workoutSessionRepository.delete(workoutSession);
	}
}
