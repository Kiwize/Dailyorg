package fr.nexa.dailyorg_java.controller.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.DTO.workout.ExerciseDTO;
import fr.nexa.dailyorg_java.DTO.workout.WorkoutRecordDTO;
import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.workout.CardioExercise;
import fr.nexa.dailyorg_java.model.workout.Exercise;
import fr.nexa.dailyorg_java.model.workout.StrengthExercise;
import fr.nexa.dailyorg_java.model.workout.WorkoutRecord;
import fr.nexa.dailyorg_java.model.workout.WorkoutSession;
import fr.nexa.dailyorg_java.service.workout.impl.AppUserService;
import fr.nexa.dailyorg_java.service.workout.impl.CardioExerciseService;
import fr.nexa.dailyorg_java.service.workout.impl.ExerciseService;
import fr.nexa.dailyorg_java.service.workout.impl.StrengthExerciseService;
import fr.nexa.dailyorg_java.service.workout.impl.WorkoutRecordService;
import fr.nexa.dailyorg_java.service.workout.impl.WorkoutSessionService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/workout")
@AllArgsConstructor
public class WorkoutSessionController {

	private final WorkoutRecordService workoutRecordService;
	private final WorkoutSessionService workoutSessionService;
	private final StrengthExerciseService strengthExerciseService;
	private final CardioExerciseService cardioExerciseService;
	private final AppUserService appUserService;
	private final ExerciseService exerciseService;

	@SuppressWarnings("rawtypes")
	@PostMapping("/get_user_workout")
	public ResponseEntity getWorkoutSessions(@RequestBody Map<String, String> data) {

		String email = data.get("email");

		try {
			return ResponseEntity.status(HttpStatus.OK).body(workoutSessionService.getWorkoutSessionsByEmail(email));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/get_workout_exercises")
	public ResponseEntity getWorkoutSessionsExercises(@RequestBody Map<String, String> data) {
		Map<String, List<WorkoutRecordDTO>> exercisesPerType;
		try {
			if (!data.containsKey("email") || !data.containsKey("workout_session_id")) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing data...");
			}

			String email = data.get("email");
			String workoutSessionId = data.get("workout_session_id");

			exercisesPerType = new HashMap<>();
			exercisesPerType.put("cardio", new ArrayList<WorkoutRecordDTO>());
			exercisesPerType.put("strength", new ArrayList<WorkoutRecordDTO>());

			List<WorkoutRecord> records = workoutRecordService
					.getRecordsByWorkoutSession(Long.parseLong(workoutSessionId));

			records.forEach(record -> {
				try {
					WorkoutRecordDTO workoutRecordDTO = new WorkoutRecordDTO();
					workoutRecordDTO.setId(record.getId());
					workoutRecordDTO.setRecordCreationDateHour(record.getRecordCreationDateHour());

					Optional<Exercise> optionalExercise = exerciseService
							.getExerciseById(record.getExerciseId().getId());
					
					if(optionalExercise.isPresent()) {
						Exercise exercise = optionalExercise.get();
						ExerciseDTO exerciseDTO = new ExerciseDTO();
						exerciseDTO.setId(exercise.getId());
						exerciseDTO.setExerciseName(exercise.getExerciseName());
						exerciseDTO.setExerciseImage(exercise.getExerciseImage());

						workoutRecordDTO.setExerciseId(exerciseDTO);
					}

					exercisesPerType
							.get(strengthExerciseService.isStrengthExercise(record.getExerciseId().getId()) ? "strength"
									: "cardio")
							.add(workoutRecordDTO);
				} catch (Exception e) {
					e.printStackTrace();
				}
			});

			return ResponseEntity.ok(exercisesPerType);
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		}
	}

	@SuppressWarnings("rawtypes")
	@PutMapping("/add_exercise_to_workout")
	public ResponseEntity addExerciseToWorkout(@RequestBody Map<String, String> data) {
		if (!data.containsKey("email") || !data.containsKey("workout_session_id") || !data.containsKey("exercise_id")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing data...");
		}
		try {
			String email = data.get("email");
			long workoutSessionId = Long.parseLong(data.get("workout_session_id"));
			long exerciseID = Long.parseLong(data.get("exercise_id"));
			String exerciseType = data.get("exercise_type");

			if (workoutRecordService.isExerciseAlreadyAdded(workoutSessionId, exerciseID))
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exercise already added !");

			if (exerciseType.equalsIgnoreCase("strength")) {
				workoutRecordService.addStrengthExerciseToWorkout(workoutSessionId,
						(StrengthExercise) strengthExerciseService.getStrengthExerciseByID(exerciseID));
				return ResponseEntity.status(HttpStatus.OK).body("Strength exercise added !");
			} else if (exerciseType.equalsIgnoreCase("cardio")) {
				int cardioTimeSpentInMins = Integer.parseInt(data.get("time_spent_in_mins"));
				int cardioIntensity = Integer.parseInt(data.get("intensity"));
				int cardioCaloriesBurnt = Integer.parseInt(data.get("calories_burnt"));

				workoutRecordService.addCardioExerciseToWorkout(workoutSessionId,
						(CardioExercise) cardioExerciseService.getCardioExerciseByID(exerciseID), cardioTimeSpentInMins,
						cardioCaloriesBurnt, cardioIntensity);
				return ResponseEntity.status(HttpStatus.OK).body("Cardio exercise added !");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Invalid exercise type : " + exerciseType + ", types allowed : strength, cardio");
			}
		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid data submitted...");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		}
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("/delete_exercise_from_workout")
	public ResponseEntity deleteExerciseFromWorkout(@RequestBody Map<String, String> data) {
		if (!data.containsKey("email") || !data.containsKey("workout_session_id") || !data.containsKey("exercise_id")) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Missing data...");
		}

		try {
			String email = data.get("email");
			long workoutSessionId = Long.parseLong(data.get("workout_session_id"));
			long exerciseID = Long.parseLong(data.get("exercise_id"));

			if (workoutRecordService.isExerciseAlreadyAdded(workoutSessionId, exerciseID)) {
				workoutRecordService.removeExerciseFromWorkout(workoutSessionId, exerciseID);
				return ResponseEntity.status(HttpStatus.OK).body("Exercise removed !");
			}

			return ResponseEntity.status(HttpStatus.OK).body("Nothing has been performed...");
		} catch (NumberFormatException e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid data submitted...");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		}
	}

	@PutMapping("/create_workout")
	public ResponseEntity<String> createWorkout(@RequestBody Map<String, String> data) {

		String email = data.get("email");

		try {
			Optional<AppUser> optionalUser = appUserService.findByEmail(email);
			if (optionalUser.isPresent()) {
				workoutSessionService.addWorkoutSession(WorkoutSession.builder().userId(optionalUser.get()).build());
				return ResponseEntity.status(HttpStatus.OK).body("");
			} else {
				return ResponseEntity.status(HttpStatus.OK).body("Invalid user...");
			}

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		}
	}

	@DeleteMapping("/delete_workout")
	public ResponseEntity<String> deleteWorkout(@RequestBody Map<String, String> data) {

		String email = data.get("email");
		if (!data.containsKey("workout_session_id")) {
			return ResponseEntity.internalServerError().body("Missing data...");
		}

		long workoutSessionID = Long.parseLong(data.get("workout_session_id"));

		try {
			workoutSessionService.deleteWorkoutSession(workoutSessionID);
			return ResponseEntity.ok("");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal error...");
		}
	}
}
