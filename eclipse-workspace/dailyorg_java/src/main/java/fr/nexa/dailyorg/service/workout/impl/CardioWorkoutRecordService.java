package fr.nexa.dailyorg.service.workout.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.workout.CardioRecord;
import fr.nexa.dailyorg.repository.workout.ICardioWorkoutRecordRepository;
import fr.nexa.dailyorg.service.workout.ICardioWorkoutRecordService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CardioWorkoutRecordService implements ICardioWorkoutRecordService {

	private final ICardioWorkoutRecordRepository cardioWorkoutRecordRepository;
	
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
