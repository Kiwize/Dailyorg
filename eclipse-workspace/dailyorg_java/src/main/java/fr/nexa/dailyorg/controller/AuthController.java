package fr.nexa.dailyorg.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.nexa.dailyorg.components.JwtRedisService;
import fr.nexa.dailyorg.config.JwtUtil;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.service.AppUserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api")
public class AuthController {
	private final AuthenticationManager authenticationManager;
	private final JwtUtil jwtUtil;
	private final BCryptPasswordEncoder passwordEncoder;

	private final AppUserService userService;

	private final JwtRedisService jwtRedisService;

	@GetMapping("/auth/status")
	public ResponseEntity<String> getAuthStatus(HttpServletRequest request) {
		System.err.println("Checking authentication status...");
		
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No authentication cookie found.");
		}

		for (Cookie cookie : cookies) {
			if ("jwt".equals(cookie.getName())) {
				String token = cookie.getValue();
				if (jwtRedisService.isTokenValid(token)) {
					return ResponseEntity.ok("User is authenticated.");
				} else {
					return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid or expired token.");
				}
			}
		}

		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No JWT cookie found.");
	}

	/**
	 * Endpoint for user login.
	 * 
	 * @param credentials A map containing "email" and "password".
	 * @return A map containing the JWT token if authentication is successful, or an
	 *         error message if not.
	 */
	@PostMapping("/login")
	public ResponseEntity<?> login(@RequestBody Map<String, String> credentials, HttpServletResponse response) {
		System.err.println(response.getStatus());

		Map<String, String> result = new HashMap<>();
		String email = credentials.get("email");
		String password = credentials.get("password");

		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
		} catch (BadCredentialsException e) {
			result.put("error", "Invalid credentials");
			return ResponseEntity.status(HttpStatus.FORBIDDEN).body(result);
		}

		String token = jwtUtil.generateToken(email);
		jwtRedisService.storeToken(token, 10, TimeUnit.HOURS);

		addJwtToCookie(response, token);

		return ResponseEntity.ok().body(result);
	}

	/**
	 * Endpoint for user registration.
	 * 
	 * @param user A map containing user details such as firstName, lastName, email,
	 *             password, and confirmPassword.
	 * @return A response entity with the JWT token if registration is successful,
	 *         or an error message if not.
	 */
	@PostMapping("/register")
	public ResponseEntity<?> register(@RequestBody Map<String, String> user, HttpServletResponse response) {
		try {
			Map<String, String> result = new HashMap<>();
		

			String firstname = user.get("firstName");
			String lastname = user.get("lastName");

			String email = user.get("email");
			String password = user.get("password");
			String confirmPassword = user.get("confirmPassword");

			if (!password.equals(confirmPassword)) {
				result.put("message", "Passwords do not match");
				return ResponseEntity.badRequest().body(result);
			}

			if (userService.findByEmail(email).isPresent()) {
				result.put("message", "Email already exists");
				return ResponseEntity.badRequest().body(result);
			}

			AppUser userToSave = AppUser.builder().username(firstname).surname(lastname).email(email)
					.password(passwordEncoder.encode(password)).role("ROLE_USER").build();

			userService.addUser(userToSave);

			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));

			// Generate JWT token after successful authentication
			String token = jwtUtil.generateToken(email);
			jwtRedisService.storeToken(token, 10, TimeUnit.HOURS);
			addJwtToCookie(response, token);

			result.put("message", "User registered successfully");
			return ResponseEntity.ok(result);
		} catch (Exception e) {
			Logger.getLogger(AuthController.class.getName()).severe("Error during user registration: " + e.getMessage());
			return ResponseEntity.badRequest().body("Internal server error during user registration...");
		}
	}

	/**
	 * Endpoint for user logout.
	 * 
	 * @param tokenData A map containing the JWT token to be invalidated.
	 * @return A response indicating whether the logout was successful or not.
	 */
	@PostMapping("/logout")
	public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
		// Get cookies from the request
		
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			return ResponseEntity.ok().body(null); // No cookies to process, just return OK
		}

		String token = null;

		for (Cookie cookie : cookies) {
			if ("jwt".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}

		if (token == null || !jwtRedisService.isTokenValid(token)) {
			return ResponseEntity.badRequest().body("Invalid or expired token.");
		}

		jwtRedisService.deleteToken(token);

		// Invalidate the cookie
		Cookie jwtCookie = new Cookie("jwt", "");
		jwtCookie.setMaxAge(0);
		jwtCookie.setHttpOnly(true);
		jwtCookie.setSecure(true);
		jwtCookie.setPath("/");

		response.addHeader("Set-Cookie", "jwt=; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=0");
		response.addCookie(jwtCookie);

		return ResponseEntity.ok("Successfully logged out.");
	}

	/**
	 * Adds a JWT token to the HTTP response as a cookie.
	 * 
	 * @param response The HTTP response to which the cookie will be added.
	 * @param jwtToken The JWT token to be added to the cookie.
	 */
	private void addJwtToCookie(HttpServletResponse response, String jwtToken) {
		Cookie cookie = new Cookie("jwt", jwtToken);
		cookie.setHttpOnly(true);
		cookie.setSecure(false);
		cookie.setPath("/");
		cookie.setMaxAge(10 * 60 * 60); // 10 hours
		response.addCookie(cookie);

		response.addHeader("Set-Cookie",
				"jwt=" + jwtToken + "; HttpOnly; Secure; SameSite=None; Path=/; Max-Age=36000");
	}
}
