package fr.nexa.dailyorg.service.dailyorg.impl;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.components.dailyorg.event.TaskChangedEvent;
import fr.nexa.dailyorg.components.dailyorg.event.TaskUpdateStatsEvent;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.model.dailyorg.Task;
import fr.nexa.dailyorg.repository.dailyorg.ITaskRepository;
import fr.nexa.dailyorg.service.dailyorg.ITaskService;
import jakarta.transaction.Transactional;

@Service
public class TaskService implements ITaskService {

	private final ApplicationEventPublisher eventPublisher;
	private final ITaskRepository taskRepository;
	
	@Autowired
	public TaskService(ApplicationEventPublisher eventPublisher, ITaskRepository taskRepository) {
		this.eventPublisher = eventPublisher;
		this.taskRepository = taskRepository;
	}

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
	public Task updateTask(Task task) throws IllegalArgumentException {
		boolean recurringTaskStateChanged = isRecurringTaskStateChanged(task); // True if the recurring task state mutated from a value to another
		boolean taskCompletionStateChanged = isTaskCompletionStateChanged(task); // True if the task completion state mutated from a value to another

		Task updatedTask = taskRepository.save(task);
		eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.UPDATED, task.getId(), recurringTaskStateChanged));
		eventPublisher.publishEvent(new TaskUpdateStatsEvent(updatedTask.getOrganizerUser().getOrganizerUserId(), 1, updatedTask.getTaskRequiredEnergy(), updatedTask.getTaskPriority().getTaskPriorityLevel(), updatedTask.isTaskCompleted(), taskCompletionStateChanged));
		return updatedTask;
	}

	@Override
	public Task updateTask(Task task, boolean triggerEvent) {
		boolean recurringTaskStateChanged = isRecurringTaskStateChanged(task); // True if the recurring task state mutated from a value to another
		boolean taskCompletionStateChanged = isTaskCompletionStateChanged(task); // True if the task completion state mutated from a value to another

		Task updatedTask = taskRepository.save(task);

		if (triggerEvent) {
			eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.UPDATED, task.getId(), recurringTaskStateChanged));
			eventPublisher.publishEvent(new TaskUpdateStatsEvent(updatedTask.getOrganizerUser().getOrganizerUserId(), 1, updatedTask.getTaskRequiredEnergy(), updatedTask.getTaskPriority().getTaskPriorityLevel(), updatedTask.isTaskCompleted(), taskCompletionStateChanged));
		}
		return updatedTask;
	}

	@Override
	@Transactional
	public void deleteTask(Task task) {
		eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
		eventPublisher.publishEvent(new TaskUpdateStatsEvent(task.getOrganizerUser().getOrganizerUserId(), 1, task.getTaskRequiredEnergy(), task.getTaskPriority().getTaskPriorityLevel(), false, true));
		taskRepository.delete(task);
	}

	@Override
	@Transactional
	public void deleteTask(Task task, boolean triggerEvent) {
		if (triggerEvent) {
			eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
			eventPublisher.publishEvent(new TaskUpdateStatsEvent(task.getOrganizerUser().getOrganizerUserId(), 1, task.getTaskRequiredEnergy(), task.getTaskPriority().getTaskPriorityLevel(), false, true));
		}

		taskRepository.delete(task);
	}

	@Override
	@Transactional
	public void deleteAllTasks(List<Task> tasks) {
		if (!tasks.isEmpty()) {
			for (Task task : tasks) {
				eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
				eventPublisher.publishEvent(new TaskUpdateStatsEvent(task.getOrganizerUser().getOrganizerUserId(), 1, task.getTaskRequiredEnergy(), task.getTaskPriority().getTaskPriorityLevel(), false, true));
			}

			taskRepository.deleteAll(tasks);
		}
	}

	@Override
	@Transactional
	public void deleteAllTasks(List<Task> tasks, boolean triggerEvent) {
		if (!tasks.isEmpty()) {
			if (triggerEvent) {
				for (Task task : tasks) {
					eventPublisher.publishEvent(new TaskChangedEvent(TaskChangedEvent.TaskChangeType.DELETED, task.getId(), false));
					eventPublisher.publishEvent(new TaskUpdateStatsEvent(task.getOrganizerUser().getOrganizerUserId(), 1, task.getTaskRequiredEnergy(), task.getTaskPriority().getTaskPriorityLevel(), false, true));
				}
			}

			taskRepository.deleteAll(tasks);
		}
	}

	@Override
	public List<Task> getAllTasksByUserId(OrganizerUser userId) {
		return taskRepository.findAllByOrganizerUser(userId);
	}

	@Override
	public List<Task> getAllTasksByUserIdAndDateRange(OrganizerUser userId, LocalDateTime startDate, LocalDateTime endDate) {
		return taskRepository.findAllByOrganizerUserAndTaskStartDateBetween(userId, startDate, endDate);
	}

	@Override
	public List<Task> getAllTasksByOcurrenceUniqueId(String occurrenceUniqueId) {
		return taskRepository.findAllByOcurrenceUniqueId(occurrenceUniqueId);
	}

	private boolean isTaskCompletionStateChanged(Task task) {
		Optional<Task> existingTaskOpt = taskRepository.findById(task.getId());
		if (existingTaskOpt.isPresent()) {
			Task existingTask = existingTaskOpt.get();
			return existingTask.isTaskCompleted() != task.isTaskCompleted();
		}

		return false;
	}

	private boolean isRecurringTaskStateChanged(Task task) {
		Optional<Task> existingTaskOpt = taskRepository.findById(task.getId());
		if (existingTaskOpt.isPresent()) {
			Task existingTask = existingTaskOpt.get();

			if (existingTask.getRecurringTaskState() == null && task.getRecurringTaskState() != null) {
				return true;
			} else if (existingTask.getRecurringTaskState() != null && task.getRecurringTaskState() == null) {
				return true;
			} else if (existingTask.getRecurringTaskState() == null && task.getRecurringTaskState() == null) {
				return false;
			} else {
				// Check if the recurring task state has changed
				if (existingTask.getRecurringTaskState().getRecurringTaskStateId() != task.getRecurringTaskState().getRecurringTaskStateId() || existingTask.getRecurringTaskState().getFrequency() != task.getRecurringTaskState().getFrequency() || existingTask.getRecurringTaskState().getTimeInterval() != task.getRecurringTaskState().getTimeInterval()
						|| !(existingTask.getRecurrenceEndDate().isEqual(task.getRecurrenceEndDate()))) {
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	public List<Task> getAllTasksByCategoryId(long idCategory) {
		return taskRepository.findAllByCategory_IdCategory(idCategory);
	}
}
