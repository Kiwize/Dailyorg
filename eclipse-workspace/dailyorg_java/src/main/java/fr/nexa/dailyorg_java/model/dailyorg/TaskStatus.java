package fr.nexa.dailyorg_java.model.dailyorg;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Data;

@Entity
@Builder
@Data
@Table(name = "TaskStatus")
public class TaskStatus {

	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long taskStatusId;
	
	@Column(nullable = false, length = 50)
	private String name;
	
	@OneToMany(mappedBy = "taskStatus")
	private List<TaskOccurence> taskOcurrences;
	
}
