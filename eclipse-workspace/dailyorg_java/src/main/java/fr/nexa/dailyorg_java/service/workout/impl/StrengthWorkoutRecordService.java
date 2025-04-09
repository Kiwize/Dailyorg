package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.StrengthRecord;
import fr.nexa.dailyorg_java.repository.workout.IStrengthWorkoutRecordRepository;
import fr.nexa.dailyorg_java.service.workout.IStrengthWorkoutRecordService;

@Service
public class StrengthWorkoutRecordService implements IStrengthWorkoutRecordService{
	
	@Autowired
	private IStrengthWorkoutRecordRepository strengthWorkoutRecordService;
	
	@Override
	public Optional<StrengthRecord> getStrengthRecordByID(Long workoutRecordID) {
		return strengthWorkoutRecordService.findById(workoutRecordID);
	}
	
	@Override
	public StrengthRecord updateStrengthRecord(StrengthRecord strengthRecord) {
		return strengthWorkoutRecordService.save(strengthRecord);
	}
	
}
