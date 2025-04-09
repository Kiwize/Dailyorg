package fr.nexa.dailyorg_java.service.workout;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.workout.StrengthRecord;

public interface IStrengthWorkoutRecordService {

	Optional<StrengthRecord> getStrengthRecordByID(Long workoutRecordID);

	StrengthRecord updateStrengthRecord(StrengthRecord strengthRecord);

}
