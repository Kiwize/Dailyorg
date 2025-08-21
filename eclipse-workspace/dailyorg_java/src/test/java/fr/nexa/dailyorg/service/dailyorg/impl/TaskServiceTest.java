package fr.nexa.dailyorg.service.dailyorg.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNullPointerException;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import fr.nexa.dailyorg.components.factory.dailyorg.RecurringTaskStateFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.TaskFactory;
import fr.nexa.dailyorg.model.dailyorg.Task;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
public class TaskServiceTest {

	private final TaskService taskService;

	private final TaskFactory taskFactory;
	
	private final RecurringTaskStateFactory recurringTaskStateFactory;
	
	@Autowired
	public TaskServiceTest(TaskService taskService, TaskFactory taskFactory, RecurringTaskStateFactory recurringTaskStateFactory) {
		this.taskService = taskService;
		this.taskFactory = taskFactory;
		this.recurringTaskStateFactory = recurringTaskStateFactory;
	}
	
	@Test
	void testGetTaskByID() {
		Task task = taskFactory.createOneTask();

		Task insertedTask = taskService.addTask(task);
		Task result = taskService.getTaskById(insertedTask.getId());

		assertThat(result.getTaskName()).isEqualTo(task.getTaskName());
	}

	@Test
	void testAddTask() {
		Task task = taskFactory.createOneTask();
		Task result = taskService.addTask(task);
		assertThat(result.getTaskName()).isEqualTo(task.getTaskName());
	}

	@Test
	void testUpdateTask() {
		Task task = taskFactory.createAndInsertOneTask();

		task.setTaskName("Updated Task Name");

		Task result = taskService.updateTask(task);

		assertThat(result.getTaskName()).isEqualTo("Updated Task Name");
	}

	@Test
	void testUpdateTask_nullTask() {
		assertThatNullPointerException().isThrownBy(() -> taskService.updateTask(null));
		taskService.updateTask(taskFactory.createOneTask());
	}

	@Test
	void testUpdateTask_taskCompletionStateChange() {
		Task task = taskFactory.createAndInsertOneTask();

		// Simulate a change in task completion state
		task.setTaskCompleted(!task.isTaskCompleted());

		Task result = taskService.updateTask(task);

		assertThat(result.getTaskName()).isEqualTo(task.getTaskName());
	}
	
	@Test
	void testUpdateTask_removeRecurringTaskStateChange() {
		Task task = taskFactory.createAndInsertOneTaskWithRecurringTaskState();

		task.setRecurringTaskState(null);
		Task result = taskService.updateTask(task);
		assertThat(result.getTaskName()).isEqualTo(task.getTaskName());
	}
	
	@Test
	void testUpdateTask_addRecurringTaskStateChange() {
		Task task = taskFactory.createAndInsertOneTask();
		
		task.setRecurringTaskState(recurringTaskStateFactory.createAndInsertOneRecurringTaskState());

		Task result = taskService.updateTask(task);

		assertThat(result.getTaskName()).isEqualTo(task.getTaskName());
	}
	
	@Test
	void testUpdateTask_recurringTaskStateChange() {
		Task task = taskFactory.createAndInsertOneTaskWithRecurringTaskState();

		task.setRecurringTaskState(recurringTaskStateFactory.createAndInsertOneRecurringTaskState());

		Task result = taskService.updateTask(task);

		assertThat(result.getTaskName()).isEqualTo(task.getTaskName());
	}
	
	@Test
	void testUpdateTask_recurringTaskStateNoChange() {
		Task task = taskFactory.createAndInsertOneTaskWithRecurringTaskState();

		Task result = taskService.updateTask(task);

		assertThat(result.getTaskName()).isEqualTo(task.getTaskName());
	}

	@Test
	void testUpdateTask_withoutEvent() {
		Task task = taskFactory.createAndInsertOneTask();

		task.setTaskName("Updated Task Name without Event");

		Task result = taskService.updateTask(task, false);

		assertThat(result.getTaskName()).isEqualTo("Updated Task Name without Event");
	}

	@Test
	void testUpdateTask_withEvent() {
		Task task = taskFactory.createAndInsertOneTask();

		task.setTaskName("Updated Task Name with Event");

		Task result = taskService.updateTask(task, true);

		assertThat(result.getTaskName()).isEqualTo("Updated Task Name with Event");
	}

	@Test
	void testDeleteTask() {
		Task task = taskFactory.createAndInsertOneTask();

		assertNotNull(taskService.getTaskById(task.getId()));

		taskService.deleteTask(task);

		assertNull(taskService.getTaskById(task.getId()));
	}

	@Test
	void testDeleteTask_withoutEvent() {
		Task task = taskFactory.createAndInsertOneTask();

		assertNotNull(taskService.getTaskById(task.getId()));

		taskService.deleteTask(task, false);

		assertNull(taskService.getTaskById(task.getId()));
	}

	@Test
	void testDeleteTask_withEvent() {
		Task task = taskFactory.createAndInsertOneTask();

		assertNotNull(taskService.getTaskById(task.getId()));

		taskService.deleteTask(task, true);

		assertNull(taskService.getTaskById(task.getId()));
	}

	@Test
	void testDeleteAllTasks() {
		List<Task> tasks = new ArrayList<>();

		taskService.deleteAllTasks(tasks);

		Task task1 = taskFactory.createAndInsertOneTask();
		Task task2 = taskFactory.createAndInsertOneTask();

		tasks.addAll(List.of(task1, task2));

		assertNotNull(taskService.getTaskById(task1.getId()));
		assertNotNull(taskService.getTaskById(task2.getId()));

		taskService.deleteAllTasks(tasks);

		assertNull(taskService.getTaskById(task1.getId()));
		assertNull(taskService.getTaskById(task2.getId()));
	}

	@Test
	void testDeleteAllTasks_withEvent() {
		List<Task> tasks = new ArrayList<>();

		taskService.deleteAllTasks(tasks, true);

		Task task1 = taskFactory.createAndInsertOneTask();
		Task task2 = taskFactory.createAndInsertOneTask();

		tasks.addAll(List.of(task1, task2));

		assertNotNull(taskService.getTaskById(task1.getId()));
		assertNotNull(taskService.getTaskById(task2.getId()));

		taskService.deleteAllTasks(tasks, true);

		assertNull(taskService.getTaskById(task1.getId()));
		assertNull(taskService.getTaskById(task2.getId()));
	}

	@Test
	void testDeleteAllTasks_withoutEvent() {
		List<Task> tasks = new ArrayList<>();

		taskService.deleteAllTasks(tasks, false);

		Task task1 = taskFactory.createAndInsertOneTask();
		Task task2 = taskFactory.createAndInsertOneTask();

		tasks.addAll(List.of(task1, task2));

		assertNotNull(taskService.getTaskById(task1.getId()));
		assertNotNull(taskService.getTaskById(task2.getId()));

		taskService.deleteAllTasks(tasks, false);

		assertNull(taskService.getTaskById(task1.getId()));
		assertNull(taskService.getTaskById(task2.getId()));
	}

	@Test
	void testGetAllTasksByUserId() {
		Task task = taskFactory.createAndInsertOneTask();

		List<Task> result = taskService.getAllTasksByUserId(task.getOrganizerUser());

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getTaskName()).isEqualTo(task.getTaskName());
	}

	@Test
	void testGetAllTasksByUserIdAndDateRange() {
		Task task = taskFactory.createAndInsertOneTask();

		List<Task> result = taskService.getAllTasksByUserIdAndDateRange(task.getOrganizerUser(), task.getTaskStartDate().minusDays(1), task.getTaskEndDate().plusDays(1));

		assertThat(result).hasSize(1);
		assertThat(result.get(0).getTaskName()).isEqualTo(task.getTaskName());
	}

	@Test
	void testGetAllTasksByOcurrenceUniqueId() {
		for (int i = 0; i < 5; i++) {
			Task task = taskFactory.createOneTask();
			task.setOcurrenceUniqueId("unique-id-12345");
			taskService.addTask(task);
		}

		List<Task> result = taskService.getAllTasksByOcurrenceUniqueId("unique-id-12345");

		assertThat(result).hasSize(5);
	}
}
