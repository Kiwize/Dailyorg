package fr.nexa.dailyorg_java.model.dailyorg;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "Badge")
public class Badge {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long badgeId;
	
	@Column(nullable = false, length = 50)
	private String badge_name;
	
	@Column(nullable = false, length = 255)
	private String badge_description;
	
	//Many to many relationship with OrganizerUser
	@ManyToMany(mappedBy = "badges")
	@JsonIgnore
	private List<OrganizerUser> organizerUsers;

}
