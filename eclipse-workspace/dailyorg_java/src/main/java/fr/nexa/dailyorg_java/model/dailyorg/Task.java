package fr.nexa.dailyorg_java.model.dailyorg;

import java.time.LocalDateTime;
import java.util.List;

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
	private String task_name;
	
	@Column(length = 255)
	private String task_description;
	
	@Column(nullable = false)
	private LocalDateTime task_creation_date;
	
	@Column()
	private LocalDateTime task_completion_date;
	
	@Column(nullable = false)
	private int task_required_energy;
	
	@Column(nullable = false)
	private LocalDateTime task_start_date;
	
	@Column(nullable = false)
	private LocalDateTime task_end_date;

	@PrePersist
	public void prePersist() {
		this.task_creation_date = LocalDateTime.now();
	}
	
	@ManyToOne
	@JoinColumn(name = "organizer_user")
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
