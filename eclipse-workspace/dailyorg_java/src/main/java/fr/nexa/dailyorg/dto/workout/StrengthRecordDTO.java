package fr.nexa.dailyorg.dto.workout;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrengthRecordDTO {
	private Long id;
	private LocalDateTime recordCreationDateHour;
	private ExerciseDTO exerciseId;
	List<WorkoutSerieDTO> workoutSeries;
	
	
}
