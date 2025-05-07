package fr.nexa.dailyorg_java.model.dailyorg;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "TaskPriority")
public class TaskPriority {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long taskPriorityId;
	
	@Column(nullable = false, length = 50)
	private String taskPriorityName;
	
	@OneToMany(mappedBy = "taskPriority")
	private List<Task> tasks;
	
}
