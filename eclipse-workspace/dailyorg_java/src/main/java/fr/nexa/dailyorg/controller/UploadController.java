package fr.nexa.dailyorg.controller;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.NoSuchAlgorithmException;
import java.util.Optional;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import fr.nexa.dailyorg.config.JwtUtil;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.service.AppUserService;
import fr.nexa.dailyorg.utils.CryptographicUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/api/upload")
public class UploadController {

	private final JwtUtil jwtUtil;

	private static final String PROFILE_PICTURE_UPLOAD_DIR = "uploads/profile_pictures/";

	private final AppUserService appUserService;

	@PostMapping("/profile_picture")
	public ResponseEntity<String> uploadProfilePicture(@RequestParam("profilePicture") MultipartFile profilePicture,
			@NonNull HttpServletRequest request) throws IOException, NoSuchAlgorithmException {
		if (profilePicture.isEmpty()) {
			return ResponseEntity.badRequest().body("No file uploaded");
		}

		File uploadDir = new File(PROFILE_PICTURE_UPLOAD_DIR);
		if (!uploadDir.exists()) {
			uploadDir.mkdirs(); // Create the directory if it doesn't exist
		}

		// Hash the user's email to create a unique filename
		String email = jwtUtil.extractUsernameFromCookies(request.getCookies());
		String fileName = CryptographicUtils.generateSHA256Hash(email) + ".webp";
		Path filePath = Paths.get(PROFILE_PICTURE_UPLOAD_DIR, fileName);
		File outputFile = new File(filePath.toString());
		
		BufferedImage img = ImageIO.read(profilePicture.getInputStream());
		ImageIO.write(img, "webp", outputFile);
		

		// Associate the uploaded file with the user in the database
		// This step should happen AFTER the user as been registered !
		try {
			Optional<AppUser> userOptional = appUserService.findByEmail(email);

			if (userOptional.isPresent()) {
				AppUser user = userOptional.get();
				user.setProfilepicturelink(filePath.toString());
				appUserService.updateUser(user);
			} else {
				return ResponseEntity.badRequest().body("User not found");
			}

		} catch (Exception e) {
			Logger.getLogger(UploadController.class.getName()).severe("Error saving profile picture: " + e.getMessage());
			return ResponseEntity.status(500).body("Error saving profile picture: " + e.getMessage());
		}

		return ResponseEntity.ok("Profile picture uploaded successfully");
	}
}
