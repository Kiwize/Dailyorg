package fr.nexa.dailyorg.dto.dailyorg;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TaskDTO {
	
	private Long taskId;
	private String taskName;
	private String taskDescription;
	private String taskCreationDate;
	private String taskCompletionDate;
	private int taskRequiredEnergy;
	private LocalDateTime taskStartDate;
	private LocalDateTime taskEndDate;

}
