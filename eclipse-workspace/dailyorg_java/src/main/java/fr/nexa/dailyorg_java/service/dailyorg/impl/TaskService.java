package fr.nexa.dailyorg_java.service.dailyorg.impl;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.components.dailyorg.TaskChangedEvent;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;
import fr.nexa.dailyorg_java.repository.dailyorg.ITaskRepository;
import fr.nexa.dailyorg_java.service.dailyorg.ITaskService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@AllArgsConstructor
@Service
public class TaskService implements ITaskService {

	private final ApplicationEventPublisher eventPublisher;

	@Autowired
	private ITaskRepository taskRepository;

	@Override
	public Task getTaskById(long id) {
		return taskRepository.findById(id).orElse(null);
	}

	@Override
	public Task addTask(Task task) {
		Task newTask = taskRepository.save(task);
		eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.CREATED, task.getId(), false));
		return newTask;
	}

	@Override
	public Task updateTask(Task task) {
		boolean recurringTaskStateChanged = isRecurringTaskStateChanged(task); // True if the recurring task state mutated from a value to another

		Task updatedTask = taskRepository.save(task);
		eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.UPDATED, task.getId(), recurringTaskStateChanged));
		return updatedTask;
	}

	@Override
	public Task updateTask(Task task, boolean triggerEvent) {
		boolean recurringTaskStateChanged = isRecurringTaskStateChanged(task); // True if the recurring task state mutated from a value to another

		Task updatedTask = taskRepository.save(task);
		if (triggerEvent)
			eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.UPDATED, task.getId(), recurringTaskStateChanged));
		return updatedTask;
	}

	@Override
	@Transactional
	public void deleteTask(Task task) {
		eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
		taskRepository.delete(task);
	}

	@Override
	@Transactional
	public void deleteTask(Task task, boolean triggerEvent) {
		if (triggerEvent)
			eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
		taskRepository.delete(task);
	}

	@Override
	@Transactional
	public void deleteAllTasks(List<Task> tasks) {
		for (Task task : tasks) {
			eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
		}
		taskRepository.deleteAll(tasks);
	}

	@Override
	@Transactional
	public void deleteAllTasks(List<Task> tasks, boolean triggerEvent) {
		if (triggerEvent)
			for (Task task : tasks)
				eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
		taskRepository.deleteAll(tasks);
	}

	@Override
	public List<Task> getAllTasksByUserId(OrganizerUser userId) {
		return taskRepository.findAllByOrganizerUser(userId);
	}

	@Override
	public List<Task> getAllTasksByUserIdAndDate(OrganizerUser userId, LocalDateTime date) {
		return taskRepository.findAllByOrganizerUserAndTaskStartDate(userId, date);
	}

	@Override
	public List<Task> getAllTasksByUserIdAndDateRange(OrganizerUser userId, LocalDateTime startDate, LocalDateTime endDate) {
		return taskRepository.findAllByOrganizerUserAndTaskStartDateBetween(userId, startDate, endDate);
	}

	@Override
	public List<Task> getAllTasksByOcurrenceUniqueId(String occurrenceUniqueId) {
		return taskRepository.findAllByOcurrenceUniqueId(occurrenceUniqueId);
	}

	private boolean isRecurringTaskStateChanged(Task task) {
		Task existingTask = taskRepository.findById(task.getId()).orElse(null);

		if (existingTask != null) {
			if (existingTask.getRecurringTaskState() == null && task.getRecurringTaskState() != null) {
				return true;
			} else if (existingTask.getRecurringTaskState() != null && task.getRecurringTaskState() == null) {
				return true;
			} else if (existingTask.getRecurringTaskState() == null && task.getRecurringTaskState() == null) {
				return false;
			}else {
				// Check if the recurring task state has changed
				if (existingTask.getRecurringTaskState().getRecurringTaskStateId() != task.getRecurringTaskState().getRecurringTaskStateId() ||
						existingTask.getRecurringTaskState().getFrequency() != task.getRecurringTaskState().getFrequency() ||
						existingTask.getRecurringTaskState().getTimeInterval() != task.getRecurringTaskState().getTimeInterval() ||
						!(existingTask.getRecurrenceEndDate().isEqual(task.getRecurrenceEndDate()))) {
					return true;
				}
			}
		}
		return false;
	}
}
