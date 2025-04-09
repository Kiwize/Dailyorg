package fr.nexa.dailyorg_java.DTO.workout;

import java.time.LocalDateTime;

import fr.nexa.dailyorg_java.model.workout.CardioRecord;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CardioRecordDTO {
	
	private Long id;
	private LocalDateTime recordCreationDateHour;
	private ExerciseDTO exerciseId;
	
	private int recordTimeSpentInMins;
    private int recordIntensity;
    private int recordCaloriesBurnt;
    
    public void setCardioRecord(CardioRecord cardioRecord) {
    	this.id = cardioRecord.getId();
    	this.recordCreationDateHour = cardioRecord.getRecordCreationDateHour();
    	this.recordTimeSpentInMins = cardioRecord.getRecordTimeSpentInMins();
    	this.recordCaloriesBurnt = cardioRecord.getRecordCaloriesBurnt();
    	this.recordIntensity = cardioRecord.getRecordIntensity();
    }
}
