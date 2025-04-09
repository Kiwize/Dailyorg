package fr.nexa.dailyorg_java.DTO.workout;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class StrengthExerciseDTO {
	private String exerciseName;
    private String exerciseImagePath;
    private List<String> muscles; 
}
