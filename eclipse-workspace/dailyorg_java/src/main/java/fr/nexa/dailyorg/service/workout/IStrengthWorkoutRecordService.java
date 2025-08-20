package fr.nexa.dailyorg.service.workout;

import java.util.Optional;

import fr.nexa.dailyorg.model.workout.StrengthRecord;

public interface IStrengthWorkoutRecordService {

	Optional<StrengthRecord> getStrengthRecordByID(Long workoutRecordID);

	StrengthRecord updateStrengthRecord(StrengthRecord strengthRecord);

}
