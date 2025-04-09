package fr.nexa.dailyorg_java.service.workout;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.workout.CardioRecord;

public interface ICardioWorkoutRecordService {

	Optional<CardioRecord> getCardioRecordByID(Long workoutRecordID);
	
	CardioRecord updateCardioRecord(CardioRecord cardioRecord);
	
}
