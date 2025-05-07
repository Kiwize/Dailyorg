package fr.nexa.dailyorg_java.controller.workout;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.service.workout.impl.AppUserService;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class AppUserController {

	private final BCryptPasswordEncoder passwordEncoder;
	private final AppUserService appUserService;

	@SuppressWarnings("rawtypes")
	@PostMapping("/create_user")
	public ResponseEntity createUser(@RequestBody Map<String, String> data) {
		try {
			if (!data.containsKey("surname") || !data.containsKey("username") || !data.containsKey("email")
					|| !data.containsKey("password")) {
				return ResponseEntity.internalServerError().body("One or multiple mandatory fields are missing...");
			}

			AppUser user = AppUser.builder().surname(data.get("surname")).username(data.get("username"))
					.email(data.get("email")).password(passwordEncoder.encode(data.get("password"))).role("ROLE_USER")
					.profilepicturelink(
							data.containsKey("profile_picture_link") ? data.get("profile_picture_link") : "NULL")
					.build();

			return ResponseEntity.ok(appUserService.addUser(user));
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}
}
