package fr.nexa.dailyorg_java.controller.dailyorg;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;
import fr.nexa.dailyorg_java.service.dailyorg.impl.OrganizerUserService;
import fr.nexa.dailyorg_java.service.dailyorg.impl.TaskService;
import fr.nexa.dailyorg_java.service.workout.impl.AppUserService;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/task")
@AllArgsConstructor
public class TaskController {

	private final TaskService taskService;
	private final AppUserService appUserService;
	private final OrganizerUserService organizerUserService;

	@PutMapping("/create_task")
	public ResponseEntity<?> createTask(@RequestBody Map<String, String> data) {
		try {
			if (!data.containsKey("task_name") || !data.containsKey("task_required_energy")
					|| !data.containsKey("user_email") || !data.containsKey("task_start_date")
					|| !data.containsKey("task_end_date")) {
				return ResponseEntity.badRequest().body("One or multiple mandatory fields are missing...");
			}

			Task task = Task.builder().taskName(data.get("task_name"))
					.taskRequiredEnergy(Integer.parseInt(data.get("task_required_energy")))
					.taskStartDate(LocalDateTime.parse(data.get("task_start_date")))
					.taskEndDate(LocalDateTime.parse(data.get("task_end_date"))).build();

			Optional<AppUser> user = appUserService.findByEmail(data.get("user_email"));

			if (user.isPresent()) {
				// Check if the user already have an organizer profile
				// If not, create one
				AppUser appUser = user.get();
				boolean hasBeenCreated = checkProfile(appUser);

				task.setOrganizerUser(appUser.getOrganizerUser());
				taskService.addTask(task);

				return ResponseEntity.ok(hasBeenCreated ? "Task and profile created..." : "Task created...");
			} else {
				return ResponseEntity.badRequest().body("User not found...");
			}

		} catch (NumberFormatException e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Invalid number format for task_required_energy...");
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}

	@GetMapping("/get_tasks_by_date")
	public ResponseEntity<?> getTasksByDate(@RequestBody Map<String, String> data) {
		try {
			if (!data.containsKey("user_email") || !data.containsKey("date")) {
				return ResponseEntity.badRequest().body("One or multiple mandatory fields are missing...");
			}

			Optional<AppUser> user = appUserService.findByEmail(data.get("user_email"));

			if (user.isPresent()) {
				AppUser appUser = user.get();
				OrganizerUser organizerUser = organizerUserService.findByAppUserId(appUser);
				return ResponseEntity.ok(
						taskService.getAllTasksByUserIdAndDate(organizerUser, LocalDateTime.parse(data.get("date"))));
			} else {
				return ResponseEntity.badRequest().body("User not found...");
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}

	/**
	 * Check if the user already have an organizer profile if not, create one
	 * 
	 * @param appUser
	 * @throws Exception
	 */
	private boolean checkProfile(AppUser appUser) throws Exception {
		if (appUser.getOrganizerUser() == null) {
			// Create an organizer profile for the user TODO change that later if needed
			OrganizerUser organizerUser = OrganizerUser.builder().appUser(appUser).energy_profile("Normal").level(1)
					.exp_points(0).build();
			appUser.setOrganizerUser(organizerUser);
			organizerUserService.create(organizerUser);
			appUserService.updateUser(appUser);
			return true;
		}

		return false;
	}

}
