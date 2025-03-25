package fr.nexa.dailyorg_java.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.CardioRecord;
import fr.nexa.dailyorg_java.repository.ICardioWorkoutRecordRepository;
import jakarta.transaction.Transactional;

@Service
public class CardioWorkoutRecordService implements ICardioWorkoutRecordService {

	@Autowired
	private ICardioWorkoutRecordRepository cardioWorkoutRecordRepository;
	
	@Override
	@Transactional
	public Optional<CardioRecord> getCardioRecordByID(Long workoutRecordID) {
		return cardioWorkoutRecordRepository.findById(workoutRecordID);
	}
	
	@Override
	public CardioRecord updateCardioRecord(CardioRecord cardioRecord) {
		return cardioWorkoutRecordRepository.save(cardioRecord);
	}
	
}
