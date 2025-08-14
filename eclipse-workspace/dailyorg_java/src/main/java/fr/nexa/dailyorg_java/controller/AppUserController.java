package fr.nexa.dailyorg_java.controller;

import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.DTO.AppUserDTO;
import fr.nexa.dailyorg_java.components.JwtRedisService;
import fr.nexa.dailyorg_java.config.JwtUtil;
import fr.nexa.dailyorg_java.mapper.AppUserMapper;
import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.service.AppUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@RestController
@RequestMapping("/api/user")
public class AppUserController {

	private final JwtRedisService jwtRedisService;
	private final JwtUtil jwtUtil;

	private final AppUserMapper appUserMapper;

	private final BCryptPasswordEncoder passwordEncoder;
	private final AppUserService appUserService;

	/**
	 * Create a new user with the provided data.
	 * 
	 * @param data A map containing user details such as surname, username, email,
	 *             password, and optionally profile_picture_link.
	 * @return ResponseEntity with the created user or an error message.
	 */
	@PostMapping("/create_user")
	public ResponseEntity<?> createUser(@RequestBody Map<String, String> data) {
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

	@PostMapping("/update_user")
	public ResponseEntity<?> updateUser(@RequestBody AppUserDTO userDTO, HttpServletRequest request,
			HttpServletResponse response) {
		try {
			String email = jwtUtil.extractUsernameFromCookies(request.getCookies());
			String newEmail = null;

			if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty()) {
				// If the user wants to change their email, we need to create a new JWT token
				// with the new email
				newEmail = userDTO.getEmail();
				if (!newEmail.equals(email)) {
					// Update the JWT token with the new email
					jwtRedisService.deleteToken(jwtUtil.extractJWTFromCookies(request.getCookies()));

					// Create a new JWT token with the new email
					// Create the new cookie
					String jwtToken = jwtUtil.generateToken(newEmail);
					Cookie cookie = new Cookie("jwt", jwtToken);
					cookie.setHttpOnly(true);
					cookie.setSecure(false);
					cookie.setPath("/");
					cookie.setMaxAge(10 * 60 * 60); // 10 hours
					response.addCookie(cookie);

					response.addHeader("Set-Cookie",
							"jwt=" + jwtToken + "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=36000");

					jwtRedisService.storeToken(jwtToken, 10, java.util.concurrent.TimeUnit.HOURS);
				}

			}

			Optional<AppUser> optionalAppuser = appUserService.findByEmail(email);

			if (optionalAppuser.isPresent()) {
				AppUser appUser = optionalAppuser.get();
				if (userDTO.getSurname() != null && !userDTO.getSurname().isEmpty()) {
					appUser.setSurname(userDTO.getSurname());
				}
				if (userDTO.getUsername() != null && !userDTO.getUsername().isEmpty()) {
					appUser.setUsername(userDTO.getUsername());
				}

				if (userDTO.getEmail() != null && !userDTO.getEmail().isEmpty() && newEmail != null) {
					appUser.setEmail(newEmail);
				}

				// Only update password if it's provided
				if (userDTO.getPassword() != null && !userDTO.getPassword().isEmpty()) {
					appUser.setPassword(passwordEncoder.encode(userDTO.getPassword()));
				}

				return ResponseEntity.ok(appUserService.updateUser(appUser));
			} else {
				return ResponseEntity.notFound().build();
			}

		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}

	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
		try {
			String email = jwtUtil.extractUsernameFromCookies(request.getCookies());
			Optional<AppUser> optionalAppuser = appUserService.findByEmail(email);
			if (optionalAppuser.isPresent()) {
				return ResponseEntity.ok(appUserMapper.toDTO(optionalAppuser.get()));
			} else {
				return ResponseEntity.notFound().build();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.internalServerError().body("Internal error...");
		}
	}

}
