package fr.nexa.dailyorg.service.workout.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.workout.StrengthRecord;
import fr.nexa.dailyorg.repository.workout.IStrengthWorkoutRecordRepository;
import fr.nexa.dailyorg.service.workout.IStrengthWorkoutRecordService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class StrengthWorkoutRecordService implements IStrengthWorkoutRecordService{
	
	private final IStrengthWorkoutRecordRepository strengthWorkoutRecordRepository;
	
	@Override
	public Optional<StrengthRecord> getStrengthRecordByID(Long workoutRecordID) {
		return strengthWorkoutRecordRepository.findById(workoutRecordID);
	}
	
	@Override
	public StrengthRecord updateStrengthRecord(StrengthRecord strengthRecord) {
		return strengthWorkoutRecordRepository.save(strengthRecord);
	}
	
}
