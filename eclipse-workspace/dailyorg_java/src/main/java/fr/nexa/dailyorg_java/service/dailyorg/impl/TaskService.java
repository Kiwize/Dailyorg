package fr.nexa.dailyorg_java.service.dailyorg.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;
import fr.nexa.dailyorg_java.repository.dailyorg.ITaskRepository;
import fr.nexa.dailyorg_java.service.dailyorg.ITaskService;

@Service
public class TaskService implements ITaskService {
	
	@Autowired
	private ITaskRepository taskRepository;

	@Override
	public Task getTaskById(long id) {
		return taskRepository.findById(id).orElse(null);
	}

	@Override
	public Task addTask(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public Task updateTask(Task task) {
		return taskRepository.save(task);
	}

	@Override
	public Task deleteTask(long id) {
		return taskRepository.findById(id).map(task -> {
			taskRepository.delete(task);
			return task;
		}).orElse(null);
	}

	@Override
	public List<Task> getAllTasksByUserId(OrganizerUser userId) {
		return taskRepository.findAllByOrganizerUser(userId);
	}

	@Override
	public List<Task> getAllTasksByUserIdAndDate(OrganizerUser userId, LocalDateTime date) {
		return taskRepository.findAllByOrganizerUserAndTaskStartDate(userId, date);
	}
	

}
