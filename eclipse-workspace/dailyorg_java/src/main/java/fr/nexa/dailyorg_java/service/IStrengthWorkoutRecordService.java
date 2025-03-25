package fr.nexa.dailyorg_java.service;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.StrengthRecord;

public interface IStrengthWorkoutRecordService {

	Optional<StrengthRecord> getStrengthRecordByID(Long workoutRecordID);

	StrengthRecord updateStrengthRecord(StrengthRecord strengthRecord);

}
