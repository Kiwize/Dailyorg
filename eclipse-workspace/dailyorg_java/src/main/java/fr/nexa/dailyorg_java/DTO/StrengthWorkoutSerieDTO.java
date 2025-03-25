package fr.nexa.dailyorg_java.DTO;

import java.util.List;

import fr.nexa.dailyorg_java.model.WorkoutSerie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrengthWorkoutSerieDTO {
	private Long strengthRecordID;
	private List<WorkoutSerie> series;
}
