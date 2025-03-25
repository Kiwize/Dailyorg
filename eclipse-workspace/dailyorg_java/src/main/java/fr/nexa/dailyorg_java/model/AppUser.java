package fr.nexa.dailyorg_java.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "AppUser")
public class AppUser {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long userId;
	
	@Column(nullable = false, length = 50)
	private String username;
	
	@Column(nullable = false, length = 50)
	private String surname;
	
	@Column(nullable = false, length = 255)
	private String password;
	
	@Column(nullable = false, length = 255)
	private String role;
	
	@Column(nullable = false, length = 255, unique = true)
	private String email;
	
	@Column(length = 255)
	private String profilepicturelink;
	
	@OneToMany(fetch = FetchType.EAGER)
	@JoinColumn(name = "userId")
	private List<WorkoutSession> workoutSessions;
}
