package fr.nexa.dailyorg_java.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class IndexController {

	@GetMapping
	public ResponseEntity<String> getMessage() {
		return ResponseEntity.ok("Hello from the backend API !");
	}
	
}
