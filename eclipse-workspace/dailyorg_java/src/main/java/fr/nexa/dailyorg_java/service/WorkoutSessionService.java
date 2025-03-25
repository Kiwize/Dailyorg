package fr.nexa.dailyorg_java.service;

import java.lang.reflect.InaccessibleObjectException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.Exercise;
import fr.nexa.dailyorg_java.model.WorkoutSession;
import fr.nexa.dailyorg_java.repository.IAppUserRepository;
import fr.nexa.dailyorg_java.repository.IWorkoutRepository;

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
	public WorkoutSession getWorkoutSessionById(Long workoutSessionId) throws Exception {
		return workoutSessionRepository.getReferenceById(workoutSessionId);
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
	public void deleteWorkoutSession(WorkoutSession workoutSession) throws Exception {
		workoutSessionRepository.delete(workoutSession);
	}
	
	

}
