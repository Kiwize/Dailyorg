package fr.nexa.dailyorg.dto.workout;

import fr.nexa.dailyorg.model.workout.WorkoutSerie;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WorkoutSerieDTO {
	
	private long workoutSerieID;
	private float repWeight;
    private int repNumber;
    private int restTimeInMins;
    
    public void setWorkoutSerie(WorkoutSerie workoutSerie) {
    	this.workoutSerieID = workoutSerie.getWorkoutSerieID();
    	this.repWeight = workoutSerie.getRepWeight();
    	this.repNumber = workoutSerie.getRepNumber();
    	this.restTimeInMins = workoutSerie.getRestTimeInMins();
    }
}
