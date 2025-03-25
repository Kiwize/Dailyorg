package fr.nexa.dailyorg_java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.StrengthRecord;
import fr.nexa.dailyorg_java.repository.IStrengthWorkoutRecordRepository;

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
