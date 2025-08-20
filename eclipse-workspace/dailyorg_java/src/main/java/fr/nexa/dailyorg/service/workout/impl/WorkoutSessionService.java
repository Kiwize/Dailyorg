package fr.nexa.dailyorg.service.workout.impl;

import java.lang.reflect.InaccessibleObjectException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.workout.Exercise;
import fr.nexa.dailyorg.model.workout.WorkoutSession;
import fr.nexa.dailyorg.repository.IAppUserRepository;
import fr.nexa.dailyorg.repository.workout.IWorkoutRepository;
import fr.nexa.dailyorg.service.workout.IWorkoutSessionService;
import fr.nexa.dailyorg.utils.EErrorMessages;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class WorkoutSessionService implements IWorkoutSessionService{
	
	private final IWorkoutRepository workoutSessionRepository;
	private final IAppUserRepository appUserRepository;

	@Override
	public List<WorkoutSession> getWorkoutSessionsByEmail(String appUserEmail) throws Exception {
		Optional<AppUser> optionalUser = appUserRepository.findByEmail(appUserEmail);
		if(optionalUser.isPresent()) {
			return workoutSessionRepository.findByUserId(optionalUser.get());
		}
		
		throw new InaccessibleObjectException(EErrorMessages.USER_NOT_FOUND.getMessage());
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
