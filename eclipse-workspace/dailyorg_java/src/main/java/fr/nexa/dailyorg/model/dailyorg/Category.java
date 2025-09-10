package fr.nexa.dailyorg.model.dailyorg;


import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Category")
@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long idCategory;
	
	@Column(nullable = false, length = 50)
	private String taskCategoryName;
	
	@Column(length = 50)
	private String taskCategoryColor;
	
	@OneToMany(mappedBy = "category")
	@JsonIgnore
	private List<Task> tasks;
	
	@ManyToOne(optional = true)
	@JsonIgnore
	private OrganizerUser organizerUser;

}
