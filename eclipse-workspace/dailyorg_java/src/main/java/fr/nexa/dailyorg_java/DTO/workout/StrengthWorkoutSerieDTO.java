package fr.nexa.dailyorg_java.DTO.workout;

import java.util.List;

import fr.nexa.dailyorg_java.model.workout.WorkoutSerie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrengthWorkoutSerieDTO {
	private Long strengthRecordID;
	private List<WorkoutSerie> series;
}
