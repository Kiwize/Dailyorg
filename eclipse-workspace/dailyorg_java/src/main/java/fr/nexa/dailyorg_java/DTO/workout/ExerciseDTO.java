package fr.nexa.dailyorg_java.DTO.workout;

import fr.nexa.dailyorg_java.model.workout.Exercise;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExerciseDTO {
	private Long id;
	private String exerciseName;
	private String exerciseImage;
	
	public void setExercise(Exercise exercise) {
		this.id = exercise.getId();
		this.exerciseImage = exercise.getExerciseImage();
		this.exerciseName = exercise.getExerciseName();
	}
}
