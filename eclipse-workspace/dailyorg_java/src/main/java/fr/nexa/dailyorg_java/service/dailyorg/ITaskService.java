package fr.nexa.dailyorg_java.service.dailyorg;

import java.time.LocalDateTime;
import java.util.List;

import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;

public interface ITaskService {
	
	Task getTaskById(long id);
	Task addTask(Task task);
	Task updateTask(Task task);
	Task deleteTask(long id);
	List<Task> getAllTasksByUserId(OrganizerUser userId);
	List<Task> getAllTasksByUserIdAndDate(OrganizerUser userId, LocalDateTime date);

}
