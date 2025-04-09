package fr.nexa.dailyorg_java.DTO.workout;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class WorkoutRecordDTO {

	private Long id;
	private LocalDateTime recordCreationDateHour;
	private ExerciseDTO exerciseId;
}
