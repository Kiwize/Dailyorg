package fr.nexa.dailyorg_java.model.dailyorg;

import java.util.List;

import fr.nexa.dailyorg_java.model.AppUser;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@SuperBuilder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class OrganizerUser extends AppUser{

	@Column(nullable = false, length = 255)
	private String energy_profile;
	
	@Column(nullable = false)
	private int level;
	
	@Column(nullable = false)
	private int exp_points;
	
	@ManyToMany()
	@JoinTable(name = "organizer_user_badge", joinColumns = @JoinColumn(name = "organizer_user_id"), inverseJoinColumns = @JoinColumn(name = "badge_id"))
	private List<Badge> badges;
	
	@OneToMany(mappedBy = "organizerUser")
	private List<Task> tasks;
	
}
