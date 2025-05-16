package fr.nexa.dailyorg_java.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.config.JwtUtil;
import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.service.workout.impl.AppUserService;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final BCryptPasswordEncoder passwordEncoder;
	
	private final AppUserService userService;

	@PostMapping("/login")
	public Map<String, String> login(@RequestBody Map<String, String> credentials) {
		Map<String, String> result = new HashMap<>();
		String email = credentials.get("email");
		String password = credentials.get("password");

		authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

		String token = jwtUtil.generateToken(email);
		result.put("token", token);
		
		return result;
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Map<String, String> user) {
		try {
			Map<String, String> result = new HashMap<>();
			
			String firstname = user.get("firstName");
			String lastname = user.get("lastName");
			
			String email = user.get("email");
			String password = user.get("password");
			String confirmPassword = user.get("confirmPassword");
			
			if(!password.equals(confirmPassword)) {
				result.put("error", "Passwords do not match");
				return ResponseEntity.badRequest().body(result);
			}
			
			if(userService.findByEmail(email).isPresent()) {
				result.put("error", "Email already exists");
				return ResponseEntity.badRequest().body(result);
			}
			
			AppUser userToSave = AppUser.builder()
					.username(firstname)
					.surname(lastname)
					.email(email)
					.password(passwordEncoder.encode(password))
					.role("ROLE_USER")
					.build();
			
			userService.addUser(userToSave);
			
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
			result.put("token", jwtUtil.generateToken(email));
			
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			e.printStackTrace();
			return ResponseEntity.badRequest().body("Internal server error during user registration...");
		}
	}
}
