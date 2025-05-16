package fr.nexa.dailyorg_java.model.dailyorg;

import java.util.List;

import fr.nexa.dailyorg_java.model.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "OrganizerUser")
public class OrganizerUser {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long organizerUserId;

	@Column(nullable = false, length = 255)
	private String energy_profile;
	
	@Column(nullable = false)
	private int level;
	
	@Column(nullable = false)
	private int exp_points;
	
	@OneToOne
	@JoinColumn(name = "userId")
	private AppUser appUser;
	
	@ManyToMany()
	@JoinTable(name = "organizer_user_badge", joinColumns = @JoinColumn(name = "organizer_user_id"), inverseJoinColumns = @JoinColumn(name = "badge_id"))
	private List<Badge> badges;
	
	@OneToMany(mappedBy = "organizerUser")
	private List<Task> tasks;
}
