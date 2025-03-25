package fr.nexa.dailyorg_java.controller;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.DTO.CardioExerciseDTO;
import fr.nexa.dailyorg_java.DTO.StrengthExerciseDTO;
import fr.nexa.dailyorg_java.model.CardioExercise;
import fr.nexa.dailyorg_java.model.Muscle;
import fr.nexa.dailyorg_java.model.StrengthExercise;
import fr.nexa.dailyorg_java.service.CardioExerciseService;
import fr.nexa.dailyorg_java.service.ExerciseService;
import fr.nexa.dailyorg_java.service.MuscleService;
import fr.nexa.dailyorg_java.service.StrengthExerciseService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/exercise")
public class ExerciseController {

	private final StrengthExerciseService strengthExerciseService;
	private final CardioExerciseService cardioExerciseService;
	private final MuscleService muscleService;
	private final ExerciseService exerciseService;

	@SuppressWarnings("rawtypes")
	@PostMapping("/get_strength_exercise_by_muscle")
	public ResponseEntity getWorkoutSessions(@RequestBody Map<String, String> data) {
		String muscleName = data.get("muscleName");

		try {
			return ResponseEntity.status(HttpStatus.OK).body(strengthExerciseService.getExercisesByMuscle(muscleService.findByName(muscleName).getId()));
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid muscle name provided...");
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/get_all_exercises")
	public ResponseEntity getAllExercises(@RequestBody Map<String, String> data) {
		try {
			if (data.containsKey("type")) {
				String type = data.get("type");
				if (!type.equalsIgnoreCase("cardio") && !type.equalsIgnoreCase("strength")) {
					return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid type provided... Allowed types : cardio, strength");
				}

				if (type.equalsIgnoreCase("cardio")) {
					return ResponseEntity.status(HttpStatus.OK).body(cardioExerciseService.getAllCardioExercises());
				} else {
					return ResponseEntity.status(HttpStatus.OK).body(strengthExerciseService.getAllStrengthExercises());
				}
			} else {
				return ResponseEntity.status(HttpStatus.OK).body(exerciseService.getAllExercises());
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		}
	}

	@PostMapping("/add_strength_exercise")
	public ResponseEntity<String> addStrengthExercise(@RequestBody StrengthExerciseDTO strengthExercise) {
		String exerciseName = strengthExercise.getExerciseName();
		String exerciseImage = strengthExercise.getExerciseImagePath();

		Set<Muscle> muscles = new HashSet<>();

		for (String s : strengthExercise.getMuscles()) {
			muscles.add(muscleService.findByName(s));
		}

		try {
			strengthExerciseService.addStrengthExercise((StrengthExercise) StrengthExercise.builder().exerciseImage(exerciseImage).exerciseName(exerciseName).muscles(muscles).build());
			return ResponseEntity.status(HttpStatus.OK).body("Successfully added !");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured...");
		}
	}

	@PostMapping("/add_cardio_exercise")
	public ResponseEntity<String> addStrengthExercise(@RequestBody CardioExerciseDTO cardioExercise) {
		String exerciseName = cardioExercise.getExerciseName();
		String exerciseImage = cardioExercise.getExerciseImagePath();

		try {
			cardioExerciseService.addCardioExercise((CardioExercise) CardioExercise.builder().exerciseImage(exerciseImage).exerciseName(exerciseName).build());
			return ResponseEntity.status(HttpStatus.OK).body("Successfully added !");
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occured...");
		}
	}
	
	
}
