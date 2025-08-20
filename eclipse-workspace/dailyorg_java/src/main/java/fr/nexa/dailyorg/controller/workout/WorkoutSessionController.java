package fr.nexa.dailyorg.controller.workout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg.DTO.workout.ExerciseDTO;
import fr.nexa.dailyorg.DTO.workout.WorkoutRecordDTO;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.workout.Exercise;
import fr.nexa.dailyorg.model.workout.WorkoutRecord;
import fr.nexa.dailyorg.model.workout.WorkoutSession;
import fr.nexa.dailyorg.service.AppUserService;
import fr.nexa.dailyorg.service.workout.impl.CardioExerciseService;
import fr.nexa.dailyorg.service.workout.impl.ExerciseService;
import fr.nexa.dailyorg.service.workout.impl.StrengthExerciseService;
import fr.nexa.dailyorg.service.workout.impl.WorkoutRecordService;
import fr.nexa.dailyorg.service.workout.impl.WorkoutSessionService;
import fr.nexa.dailyorg.utils.EErrorMessages;
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
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/get_workout_exercises")
	public ResponseEntity getWorkoutSessionsExercises(@RequestBody Map<String, String> data) {
		Map<String, List<WorkoutRecordDTO>> exercisesPerType;
		try {
			if (!data.containsKey("email") || !data.containsKey(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName())) {
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EErrorMessages.INVALID_INPUT.getMessage());
			}

			String workoutSessionId = data.get(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName());

			exercisesPerType = new HashMap<>();
			exercisesPerType.put(EWorkoutControllerFields.CARDIO.getFieldName(), new ArrayList<>());
			exercisesPerType.put(EWorkoutControllerFields.STRENGTH.getFieldName(), new ArrayList<>());

			List<WorkoutRecord> records = workoutRecordService.getRecordsByWorkoutSession(Long.parseLong(workoutSessionId));

			records.forEach(record -> {
				try {
					WorkoutRecordDTO workoutRecordDTO = new WorkoutRecordDTO();
					workoutRecordDTO.setId(record.getId());
					workoutRecordDTO.setRecordCreationDateHour(record.getRecordCreationDateHour());

					Optional<Exercise> optionalExercise = exerciseService.getExerciseById(record.getExerciseId().getId());

					if (optionalExercise.isPresent()) {
						Exercise exercise = optionalExercise.get();
						ExerciseDTO exerciseDTO = new ExerciseDTO();
						exerciseDTO.setId(exercise.getId());
						exerciseDTO.setExerciseName(exercise.getExerciseName());
						exerciseDTO.setExerciseImage(exercise.getExerciseImage());

						workoutRecordDTO.setExerciseId(exerciseDTO);
					}

					exercisesPerType.get(strengthExerciseService.isStrengthExercise(record.getExerciseId().getId()) ? EWorkoutControllerFields.STRENGTH.getFieldName() : EWorkoutControllerFields.CARDIO.getFieldName()).add(workoutRecordDTO);
				} catch (Exception e) {
					Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage() + e.getMessage());
				}
			});

			return ResponseEntity.ok(exercisesPerType);
		} catch (Exception e) {
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INVALID_INPUT.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@PutMapping("/add_exercise_to_workout")
	public ResponseEntity addExerciseToWorkout(@RequestBody Map<String, String> data) {
		if (!data.containsKey("email") || !data.containsKey(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName()) || !data.containsKey(EWorkoutControllerFields.EXERCISE_ID.getFieldName())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EErrorMessages.DATA_NOT_FOUND.getMessage());
		}
		try {
			long workoutSessionId = Long.parseLong(data.get(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName()));
			long exerciseID = Long.parseLong(data.get(EWorkoutControllerFields.EXERCISE_ID.getFieldName()));
			String exerciseType = data.get(EWorkoutControllerFields.EXERCISE_TYPE.getFieldName());

			if (workoutRecordService.isExerciseAlreadyAdded(workoutSessionId, exerciseID))
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Exercise already added !");

			if (exerciseType.equalsIgnoreCase(EWorkoutControllerFields.STRENGTH.getFieldName())) {
				workoutRecordService.addStrengthExerciseToWorkout(workoutSessionId, strengthExerciseService.getStrengthExerciseByID(exerciseID));
				return ResponseEntity.status(HttpStatus.OK).body("Strength exercise added !");
			} else if (exerciseType.equalsIgnoreCase(EWorkoutControllerFields.CARDIO.getFieldName())) {
				int cardioTimeSpentInMins = Integer.parseInt(data.get(EWorkoutControllerFields.TIME_SPENT_IN_MINS.getFieldName()));
				int cardioIntensity = Integer.parseInt(data.get(EWorkoutControllerFields.INTENSITY.getFieldName()));
				int cardioCaloriesBurnt = Integer.parseInt(data.get(EWorkoutControllerFields.CALORIES_BURNT.getFieldName()));

				workoutRecordService.addCardioExerciseToWorkout(workoutSessionId, cardioExerciseService.getCardioExerciseByID(exerciseID), cardioTimeSpentInMins, cardioCaloriesBurnt, cardioIntensity);
				return ResponseEntity.status(HttpStatus.OK).body("Cardio exercise added !");
			} else {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INVALID_EXERCISE_TYPE.getMessage());
			}
		} catch (NumberFormatException e) {
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INVALID_INPUT.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INVALID_INPUT.getMessage());
		} catch (Exception e) {
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
		}
	}

	@SuppressWarnings("rawtypes")
	@DeleteMapping("/delete_exercise_from_workout")
	public ResponseEntity deleteExerciseFromWorkout(@RequestBody Map<String, String> data) {
		if (!data.containsKey("email") || !data.containsKey(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName()) || !data.containsKey(EWorkoutControllerFields.EXERCISE_ID.getFieldName())) {
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EErrorMessages.DATA_NOT_FOUND.getMessage());
		}

		try {
			long workoutSessionId = Long.parseLong(data.get(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName()));
			long exerciseID = Long.parseLong(data.get(EWorkoutControllerFields.EXERCISE_ID.getFieldName()));

			if (workoutRecordService.isExerciseAlreadyAdded(workoutSessionId, exerciseID)) {
				workoutRecordService.removeExerciseFromWorkout(workoutSessionId, exerciseID);
				return ResponseEntity.status(HttpStatus.OK).body("Exercise removed !");
			}

			return ResponseEntity.status(HttpStatus.OK).body("Nothing has been performed...");
		} catch (NumberFormatException e) {
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INVALID_INPUT.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INVALID_INPUT.getMessage());
		} catch (Exception e) {
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
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
				return ResponseEntity.status(HttpStatus.FORBIDDEN).body(EErrorMessages.USER_NOT_FOUND.getMessage());
			}

		} catch (Exception e) {
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
		}
	}

	@DeleteMapping("/delete_workout")
	public ResponseEntity<String> deleteWorkout(@RequestBody Map<String, String> data) {

		if (!data.containsKey(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName())) {
			return ResponseEntity.internalServerError().body(EErrorMessages.DATA_NOT_FOUND.getMessage());
		}

		long workoutSessionID = Long.parseLong(data.get(EWorkoutControllerFields.WORKOUT_SESSION_ID.getFieldName()));

		try {
			workoutSessionService.deleteWorkoutSession(workoutSessionID);
			return ResponseEntity.ok("");
		} catch (Exception e) {
			Logger.getLogger(WorkoutSessionController.class.getName()).severe(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage() + e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(EErrorMessages.INTERNAL_SERVER_ERROR.getMessage());
		}
	}
}
