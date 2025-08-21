package fr.nexa.dailyorg.service.dailyorg;

import java.time.LocalDateTime;
import java.util.List;

import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.model.dailyorg.Task;

public interface ITaskService {
	
	Task getTaskById(long id);
	Task addTask(Task task);
	Task updateTask(Task task);
	Task updateTask(Task task, boolean triggerEvent);
	void deleteTask(Task task);
	void deleteTask(Task task, boolean triggerEvent);
	void deleteAllTasks(List<Task> tasks);
	void deleteAllTasks(List<Task> tasks, boolean triggerEvent);
	List<Task> getAllTasksByUserId(OrganizerUser userId);
	List<Task> getAllTasksByUserIdAndDateRange(OrganizerUser userId, LocalDateTime startDate, LocalDateTime endDate);
	List<Task> getAllTasksByOcurrenceUniqueId(String occurrenceUniqueId);

}
