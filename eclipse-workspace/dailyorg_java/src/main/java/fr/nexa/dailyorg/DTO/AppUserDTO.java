package fr.nexa.dailyorg.DTO;

import fr.nexa.dailyorg.DTO.dailyorg.OrganizerUserDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppUserDTO {
	
	private String surname;
	private String username;
	private String email;
	private String password;
	private String profilePictureLink;
	private OrganizerUserDTO organizerUser; // Optional, can be null

	// Constructor
	public AppUserDTO(String surname, String username, String email, String password, String profilePictureLink) {
		this.surname = surname;
		this.username = username;
		this.email = email;
		this.password = password;
		this.profilePictureLink = profilePictureLink;
	}

	// Default constructor
	public AppUserDTO() {
	}
}
