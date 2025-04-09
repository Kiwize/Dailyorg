package fr.nexa.dailyorg_java.controller.workout;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.service.workout.impl.MuscleService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/muscles")
public class MuscleController {

	private final MuscleService muscleService;
	
	@SuppressWarnings("rawtypes")
	@PostMapping("/get_all_muscles")
	public ResponseEntity getAllMuscles(@RequestBody Map<String, String> data) {
		try {
			return ResponseEntity.ok(muscleService.findAll());
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}
	
}
