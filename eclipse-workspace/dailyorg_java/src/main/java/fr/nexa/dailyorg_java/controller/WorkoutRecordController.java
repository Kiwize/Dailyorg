package fr.nexa.dailyorg_java.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.DTO.StrengthWorkoutSerieDTO;
import fr.nexa.dailyorg_java.model.CardioRecord;
import fr.nexa.dailyorg_java.model.StrengthRecord;
import fr.nexa.dailyorg_java.model.WorkoutSerie;
import fr.nexa.dailyorg_java.service.CardioWorkoutRecordService;
import fr.nexa.dailyorg_java.service.StrengthWorkoutRecordService;
import fr.nexa.dailyorg_java.service.WorkoutSerieService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/record")
public class WorkoutRecordController {

	private final CardioWorkoutRecordService cardioWorkoutRecordService;
	private final StrengthWorkoutRecordService strengthWorkoutRecordService;
	private final WorkoutSerieService workoutSerieService;

	@PostMapping("/update_cardio_record")
	public ResponseEntity<String> updateCardioRecord(@RequestBody Map<String, String> data) {
		long cardioRecordID = Long.parseLong(data.get("cardioRecordID"));
		int newTimeSpent = data.containsKey("time_spent_in_mins") ? Integer.parseInt(data.get("time_spent_in_mins")) : -1;
		int newIntensity = data.containsKey("intensity") ? Integer.parseInt(data.get("intensity")) : -1;
		int newCaloriesBurnt = data.containsKey("calories_burnt") ? Integer.parseInt(data.get("calories_burnt")) : -1;

		Optional<CardioRecord> optionalCardioRecord = cardioWorkoutRecordService.getCardioRecordByID(cardioRecordID);

		if (optionalCardioRecord.isPresent()) {
			CardioRecord cardioRecord = optionalCardioRecord.get();
			if (newCaloriesBurnt != -1)
				cardioRecord.setRecordCaloriesBurnt(newCaloriesBurnt);
			if (newIntensity != -1)
				cardioRecord.setRecordIntensity(newIntensity);
			if (newTimeSpent != -1)
				cardioRecord.setRecordTimeSpentInMins(newTimeSpent);

			cardioWorkoutRecordService.updateCardioRecord(cardioRecord);
			return ResponseEntity.ok("Cardio record updated!");
		} else {
			return ResponseEntity.internalServerError().body("The cardio record couldn't be found...");
		}
	}

	@PostMapping("/add_strength_serie")
	public ResponseEntity<String> addStrengthSerie(@RequestBody StrengthWorkoutSerieDTO strengthWorkoutSerieDTO) {
		try {
			Optional<StrengthRecord> optionalStrengthRecord = strengthWorkoutRecordService.getStrengthRecordByID(strengthWorkoutSerieDTO.getStrengthRecordID());

			if (optionalStrengthRecord.isPresent()) {
				StrengthRecord strengthRecord = optionalStrengthRecord.get();
				for (WorkoutSerie s : strengthWorkoutSerieDTO.getSeries()) {
					s.setStrengthRecordId(strengthRecord);
					workoutSerieService.addWorkoutSerie(s);
				}

				return ResponseEntity.ok("Strength record updated!");
			} else {
				return ResponseEntity.internalServerError().body("The strength record couldn't be found...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}

	@PostMapping("/delete_strength_serie")
	public ResponseEntity<String> deleteStrengthSerie(@RequestBody Map<String, String> data) {
		try {
			Long strengthRecordID = Long.parseLong(data.get("workoutSerieID"));
			if(!data.containsKey("workoutSerieID")) {
				return ResponseEntity.internalServerError().body("Missing data...");
			}

			workoutSerieService.deleteWorkoutSerie(strengthRecordID);

			return ResponseEntity.ok("Strength record deleted!");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}
	
	@PostMapping("/update_strength_serie")
	public ResponseEntity<String> updateStrengthSerie(@RequestBody Map<String, String> data) {
		try {
			Long strengthRecordID = Long.parseLong(data.get("workoutSerieID"));
			if(!data.containsKey("workoutSerieID")) {
				return ResponseEntity.internalServerError().body("Missing data...");
			}
			
			int repNumber = data.containsKey("repNumber") ? Integer.parseInt(data.get("repNumber")) : -1;
			int repWeight = data.containsKey("repWeight") ? Integer.parseInt(data.get("repWeight")) : -1;
			int restTimeInMins = data.containsKey("restTimeInMins") ? Integer.parseInt(data.get("restTimeInMins")) : -1;
			
			Optional<WorkoutSerie> optionalWorkoutSerie = workoutSerieService.getWorkoutSerie(strengthRecordID);
			
			if(optionalWorkoutSerie.isPresent()) {
				WorkoutSerie workoutSerie = optionalWorkoutSerie.get();
				
				if(repNumber >= 0)
					workoutSerie.setRepNumber(repNumber);
				if(repWeight >= 0)
					workoutSerie.setRepWeight(repWeight);
				if(restTimeInMins >= 0) 
					workoutSerie.setRestTimeInMins(restTimeInMins);
				
				workoutSerieService.updateWorkoutSerie(workoutSerie);
				return ResponseEntity.ok("Strength record updated!");
			} else {
				return ResponseEntity.internalServerError().body("Strength serie not found...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}


	@SuppressWarnings("rawtypes")
	@PostMapping("/get_cardio_record")
	public ResponseEntity getCardioRecord(@RequestBody Map<String, String> data) {
		try {
			long cardioRecordID = Long.parseLong(data.get("cardioRecordID"));
			if (!data.containsKey("cardioRecordID")) {
				return ResponseEntity.internalServerError().body("Missing data...");
			}

			Optional<CardioRecord> optionalCardioRecord = cardioWorkoutRecordService.getCardioRecordByID(cardioRecordID);

			if (optionalCardioRecord.isPresent()) {
				CardioRecord cardioRecord = optionalCardioRecord.get();
				return ResponseEntity.status(HttpStatus.OK).body(cardioRecord);
			} else {
				return ResponseEntity.internalServerError().body("The cardio record couldn't be found...");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Invalid data submitted...");
		}
	}

	@SuppressWarnings("rawtypes")
	@PostMapping("/get_strength_record")
	public ResponseEntity getStrengthRecord(@RequestBody Map<String, String> data) {
		try {
			long strengthRecordID = Long.parseLong(data.get("strengthRecordID"));
			if (!data.containsKey("strengthRecordID")) {
				return ResponseEntity.internalServerError().body("Missing data...");
			}

			Optional<StrengthRecord> optionalStrengthRecord = strengthWorkoutRecordService.getStrengthRecordByID(strengthRecordID);

			if (optionalStrengthRecord.isPresent()) {
				StrengthRecord strengthRecord = optionalStrengthRecord.get();
				return ResponseEntity.status(HttpStatus.OK).body(strengthRecord);
			} else {
				return ResponseEntity.internalServerError().body("The strength record couldn't be found...");
			}
		} catch (NumberFormatException e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Invalid data submitted...");
		}
	}

}
