package fr.nexa.dailyorg_java.model.dailyorg;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@Getter @Setter @AllArgsConstructor @NoArgsConstructor
@Table(name = "Task")
public class Task {
	
	@Id @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long taskId;
	
	@Column(nullable = false, length = 50)
	private String taskName;
	
	@Column(length = 255)
	private String taskDescription;
	
	@Column(nullable = false)
	private LocalDateTime taskCreationDate;
	
	@Column()
	private LocalDateTime taskCompletionDate;
	
	@Column(nullable = false)
	private int taskRequiredEnergy;
	
	@Column(nullable = false)
	private LocalDateTime taskStartDate;
	
	@Column(nullable = false)
	private LocalDateTime taskEndDate;

	@PrePersist
	public void prePersist() {
		this.taskCreationDate = LocalDateTime.now();
	}
	
	@ManyToOne
	@JoinColumn(name = "organizer_user")
	@JsonIgnore
	private OrganizerUser organizerUser;
	
	@ManyToOne
	@JoinColumn(name = "task_priority")
	private TaskPriority taskPriority;
	
	@ManyToOne
	@JoinColumn(name = "recurring_task_state")
	private RecurringTaskState recurringTaskState;
	
	@OneToMany(mappedBy = "taskId")
	private List<TaskOccurence> taskOccurences;
}
