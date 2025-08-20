package fr.nexa.dailyorg.service.workout;

import java.util.Optional;

import fr.nexa.dailyorg.model.workout.CardioRecord;

public interface ICardioWorkoutRecordService {

	Optional<CardioRecord> getCardioRecordByID(Long workoutRecordID);
	
	CardioRecord updateCardioRecord(CardioRecord cardioRecord);
	
}
