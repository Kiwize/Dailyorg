package fr.nexa.dailyorg_java.service.dailyorg;

import java.util.List;
import java.util.Optional;

import fr.nexa.dailyorg_java.model.dailyorg.TaskPriority;

public interface ITaskPriorityService {

	/**
	 * Create a new task priority.
	 * 
	 * @param taskPriorityName the name of the task priority
	 * @return the created TaskPriority object
	 */
	TaskPriority createTaskPriority(String taskPriorityName);
	
	/**
	 * Get a task priority by its name.
	 * 
	 * @param taskPriorityName the name of the task priority
	 * @return an Optional containing the TaskPriority object if found, or empty if not found
	 */
	Optional<TaskPriority> getTaskPriorityByTaskPriorityName(String taskPriorityName);

	/**
	 * Get all task priorities.
	 * 
	 * @return a list of all TaskPriority objects
	 */
	List<TaskPriority> getAllTaskPriorities();
	
	Optional<TaskPriority> findTaskPriorityById(long taskPriorityId);
}
