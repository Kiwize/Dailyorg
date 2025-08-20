package fr.nexa.dailyorg.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg.model.workout.CardioRecord;

@Repository
public interface ICardioWorkoutRecordRepository extends JpaRepository<CardioRecord, Long>{

}
