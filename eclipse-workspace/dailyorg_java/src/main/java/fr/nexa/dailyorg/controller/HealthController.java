package fr.nexa.dailyorg.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping("/api/health")
@AllArgsConstructor
public class HealthController {

	/**
	 * Endpoint to check the health of the application.
	 * 
	 * @return A simple message indicating that the application is running.
	 */
	@GetMapping("/")
	public String healthCheck() {
		return "Ja Ja.. Backend very gut gut !";
	}
}
