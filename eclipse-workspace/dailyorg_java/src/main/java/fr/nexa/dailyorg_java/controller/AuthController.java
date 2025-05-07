package fr.nexa.dailyorg_java.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg_java.config.JwtUtil;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;

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
}
