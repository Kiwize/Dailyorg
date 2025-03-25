package fr.nexa.dailyorg_java.service;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.CardioRecord;

public interface ICardioWorkoutRecordService {

	Optional<CardioRecord> getCardioRecordByID(Long workoutRecordID);
	
	CardioRecord updateCardioRecord(CardioRecord cardioRecord);
	
}
