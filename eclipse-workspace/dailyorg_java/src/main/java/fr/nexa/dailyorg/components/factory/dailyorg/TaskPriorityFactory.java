package fr.nexa.dailyorg.components.factory.dailyorg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.model.dailyorg.TaskPriority;
import fr.nexa.dailyorg.repository.dailyorg.ITaskPriorityRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class TaskPriorityFactory {

	private final ITaskPriorityRepository taskPriorityRepository;
	
	public TaskPriority createOneTaskPriority() {
		return TaskPriority.builder()
				.taskPriorityLevel((int) (Math.random() * 3)) // Random priority level between 0 and 3
				.taskPriorityName("Low")
				.build();
	}
	
	public TaskPriority createAndInsertOneTaskPriority() {
		TaskPriority taskPriority = createOneTaskPriority();
		return taskPriorityRepository.save(taskPriority);
	}
	
}
