package fr.nexa.dailyorg.components.factory.dailyorg;

import fr.nexa.dailyorg.model.dailyorg.TaskPriority;

public class TaskPriorityFactory {

	public TaskPriority createOneTaskPriority() {
		return TaskPriority.builder().taskPriorityLevel((int) (Math.random() * 3)) // Random priority level between 0 and 3
				.taskPriorityName("Low").build();
	}
}
