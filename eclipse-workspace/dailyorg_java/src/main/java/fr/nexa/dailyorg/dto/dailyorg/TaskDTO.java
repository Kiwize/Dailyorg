package fr.nexa.dailyorg.dto.dailyorg;

import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
	
	private Long taskId;
	private Long taskCategoryId;
	
	private Boolean isTaskCompleted;
	
	@NotNull
	private String taskName;
	
	@NotNull
	private LocalDateTime taskStartDate;
	
	@NotNull
	private LocalDateTime taskEndDate;
	
	private String taskDescription;
	
	@NotNull
	private int taskRequiredEnergy;
	
	@NotNull
	private String taskPriority;
	
	private Boolean isRecurrent;
	
	private Long taskRepeatFrequencyId;
	
	private LocalDate taskRepeatEndDate;
	
	
	private String taskCreationDate;
	private String taskCompletionDate;
}
