package fr.nexa.dailyorg.components.factory.dailyorg;

import java.util.Random;

import fr.nexa.dailyorg.model.dailyorg.TaskPriority;

public class TaskPriorityFactory {
	
	private final Random random = new Random();

	public TaskPriority createOneTaskPriority() {
		return TaskPriority.builder().taskPriorityLevel(random.nextInt(4)) // Random priority level between 0 and 3
				.taskPriorityName("Low").build();
	}
	
	public TaskPriority createOneTaskPriority(String name, int level) {
		return TaskPriority.builder().taskPriorityLevel(level) // Random priority level between 0 and 3
				.taskPriorityName(name).build();
	}
	
}
