package fr.nexa.dailyorg.service.dailyorg.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import fr.nexa.dailyorg.components.dailyorg.event.TaskChangedEvent;
import fr.nexa.dailyorg.components.factory.AppUserFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.OrganizerUserFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.TaskFactory;
import fr.nexa.dailyorg.components.factory.dailyorg.TaskPriorityFactory;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.model.dailyorg.Task;
import fr.nexa.dailyorg.repository.dailyorg.IRecurringTaskStateRepository;
import fr.nexa.dailyorg.repository.dailyorg.ITaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {

	@InjectMocks
	private TaskService taskService;

	// Repositories

	@Mock
	private ITaskRepository taskRepository;

	@Mock
	private IRecurringTaskStateRepository recurringTaskStateRepository;

	@Mock
	private ApplicationEventPublisher eventPublisher;

	// Factories
	private final TaskFactory taskFactory = new TaskFactory();
	private final OrganizerUserFactory organizerUserFactory = new OrganizerUserFactory();
	private final AppUserFactory appUserFactory = new AppUserFactory();
	private final TaskPriorityFactory taskPriorityFactory = new TaskPriorityFactory();
	
	@Test
	public void testGetTaskById() {
		Task task = taskFactory.createOneTask();
		
		// Mock the repository call
		when(taskRepository.findById(task.getId())).thenReturn(java.util.Optional.of(task));

		Task foundTask = taskService.getTaskById(task.getId());

		assertEquals(task, foundTask);
	}
	
	@Test
	public void testAddTask() {
		Task task = taskFactory.createOneTask();
		task.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		taskService.addTask(task);
		verify(taskRepository).save(task);

		// Verify that the event is published
		ArgumentCaptor<TaskChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskChangedEvent.class);
		verify(eventPublisher).publishEvent(eventCaptor.capture());
		
		TaskChangedEvent event = eventCaptor.getValue();
		
		// Verify the event properties
		assertEquals(TaskChangedEvent.TaskChangeType.CREATED, event.getChangeType());
		assertEquals(task.getId(), event.getTaskId());
	}
	
	@Test
	public void testUpdateTask() {
		Task task = taskFactory.createOneTask();
		task.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		// Mock the repository call
		when(taskRepository.save(task)).thenReturn(task);
		taskService.updateTask(task);

		verify(taskRepository).save(task);

		// Verify that the event is published
		ArgumentCaptor<TaskChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskChangedEvent.class);
		verify(eventPublisher).publishEvent(eventCaptor.capture());
		
		TaskChangedEvent event = eventCaptor.getValue();
		
		// Verify the event properties
		assertEquals(TaskChangedEvent.TaskChangeType.UPDATED, event.getChangeType());
		assertEquals(task.getId(), event.getTaskId());
	}
	
	@Test
	public void testUpdateTaskWithTriggerEvent() {
		Task task = taskFactory.createOneTask();
		task.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		// Mock the repository call
		when(taskRepository.save(task)).thenReturn(task);
		taskService.updateTask(task, true);

		verify(taskRepository).save(task);

		// Verify that the event is published
		ArgumentCaptor<TaskChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskChangedEvent.class);
		verify(eventPublisher).publishEvent(eventCaptor.capture());
		
		TaskChangedEvent event = eventCaptor.getValue();
		
		// Verify the event properties
		assertEquals(TaskChangedEvent.TaskChangeType.UPDATED, event.getChangeType());
		assertEquals(task.getId(), event.getTaskId());
	}
	
	@Test
	public void testUpdateTaskWithoutTriggerEvent() {
		Task task = taskFactory.createOneTask();
		task.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		// Mock the repository call
		when(taskRepository.save(task)).thenReturn(task);
		taskService.updateTask(task, false);

		verify(taskRepository).save(task);

		// Verify that the event is published
		verify(eventPublisher, never()).publishEvent(any());
	}

	@Test
	public void testDeleteTask() {
		Task task = taskFactory.createOneTask();
		task.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		taskService.deleteTask(task);

		verify(taskRepository).delete(task);

		// Verify that the event is published
		ArgumentCaptor<TaskChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskChangedEvent.class);
		verify(eventPublisher).publishEvent(eventCaptor.capture());
		
		TaskChangedEvent event = eventCaptor.getValue();
		
		// Verify the event properties
		assertEquals(TaskChangedEvent.TaskChangeType.DELETED, event.getChangeType());
		assertEquals(task.getId(), event.getTaskId());
	}
	
	@Test
	public void testDeleteTaskWithTriggerEvent() {
		Task task = taskFactory.createOneTask();
		task.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		taskService.deleteTask(task, true);

		verify(taskRepository).delete(task);

		// Verify that the event is published
		ArgumentCaptor<TaskChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskChangedEvent.class);
		verify(eventPublisher).publishEvent(eventCaptor.capture());
		
		TaskChangedEvent event = eventCaptor.getValue();
		
		// Verify the event properties
		assertEquals(TaskChangedEvent.TaskChangeType.DELETED, event.getChangeType());
		assertEquals(task.getId(), event.getTaskId());
	}
	
	@Test
	public void testDeleteTaskWithoutTriggerEvent() {
		Task task = taskFactory.createOneTask();
		task.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		taskService.deleteTask(task, false);

		verify(taskRepository).delete(task);

		// Verify that the event is not published
		verify(eventPublisher, never()).publishEvent(any());
	}
	
	@Test
	public void testDeleteAllTasks() {
		Task task1 = taskFactory.createOneTask();
		Task task2 = taskFactory.createOneTask();
		task1.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task2.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task1.setTaskPriority(taskPriorityFactory.createOneTaskPriority());
		task2.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		taskService.deleteAllTasks(java.util.List.of(task1, task2));

		verify(taskRepository).deleteAll(java.util.List.of(task1, task2));

		// Verify that the events are published
		ArgumentCaptor<TaskChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskChangedEvent.class);
		verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());
		
		assertEquals(2, eventCaptor.getAllValues().size());
		
		for (TaskChangedEvent event : eventCaptor.getAllValues()) {
			assertEquals(TaskChangedEvent.TaskChangeType.DELETED, event.getChangeType());
			assertEquals(event.getTaskId(), task1.getId() == event.getTaskId() ? task1.getId() : task2.getId());
		}
	}
	
	@Test
	public void testDeleteAllTasks_emptyList() {
		taskService.deleteAllTasks(java.util.List.of());
		verify(taskRepository, never()).deleteAll(any());
		verify(eventPublisher, never()).publishEvent(any());
	}
	
	@Test
	public void testDeleteAllTasksWithTriggerEvent() {
		Task task1 = taskFactory.createOneTask();
		Task task2 = taskFactory.createOneTask();
		task1.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task2.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task1.setTaskPriority(taskPriorityFactory.createOneTaskPriority());
		task2.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		taskService.deleteAllTasks(java.util.List.of(task1, task2), true);

		verify(taskRepository).deleteAll(java.util.List.of(task1, task2));

		// Verify that the events are published
		ArgumentCaptor<TaskChangedEvent> eventCaptor = ArgumentCaptor.forClass(TaskChangedEvent.class);
		verify(eventPublisher, times(2)).publishEvent(eventCaptor.capture());
		
		assertEquals(2, eventCaptor.getAllValues().size());
		
		for (TaskChangedEvent event : eventCaptor.getAllValues()) {
			assertEquals(TaskChangedEvent.TaskChangeType.DELETED, event.getChangeType());
			assertEquals(event.getTaskId(), task1.getId() == event.getTaskId() ? task1.getId() : task2.getId());
		}
	}
	
	@Test
	public void testDeleteAllTasksWithoutTriggerEvent() {
		Task task1 = taskFactory.createOneTask();
		Task task2 = taskFactory.createOneTask();
		task1.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task2.setOrganizerUser(organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser()));
		task1.setTaskPriority(taskPriorityFactory.createOneTaskPriority());
		task2.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		taskService.deleteAllTasks(java.util.List.of(task1, task2), false);

		verify(taskRepository).deleteAll(java.util.List.of(task1, task2));

		// Verify that the events are not published
		verify(eventPublisher, never()).publishEvent(any());
	}
	
	@Test
	public void testDeleteAllTasksWithEvent_emptyList() {
		taskService.deleteAllTasks(java.util.List.of(), true);
		verify(taskRepository, never()).deleteAll(any());
		verify(eventPublisher, never()).publishEvent(any());
	}
	
	@Test
	public void testGetAllTasksByUserId() {
		Task task1 = taskFactory.createOneTask();
		Task task2 = taskFactory.createOneTask();
		
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser());
		
		task1.setOrganizerUser(organizerUser);
		task2.setOrganizerUser(organizerUser);
		task1.setTaskPriority(taskPriorityFactory.createOneTaskPriority());
		task2.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		when(taskRepository.findAllByOrganizerUser(organizerUser)).thenReturn(java.util.List.of(task1, task2));

		java.util.List<Task> tasks = taskService.getAllTasksByUserId(task1.getOrganizerUser());

		assertEquals(2, tasks.size());
		assertEquals(task1, tasks.get(0));
		assertEquals(task2, tasks.get(1));
	}
	
	@Test
	public void testGetAllTasksByUserId_emptyList() {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser());
		
		when(taskRepository.findAllByOrganizerUser(organizerUser)).thenReturn(java.util.List.of());

		java.util.List<Task> tasks = taskService.getAllTasksByUserId(organizerUser);

		assertEquals(0, tasks.size());
		verify(taskRepository).findAllByOrganizerUser(organizerUser);
	}
	
	@Test
	public void testGetAllTasksByUserId_nullUser() {
		java.util.List<Task> tasks = taskService.getAllTasksByUserId(null);
		assertEquals(0, tasks.size());
		verify(taskRepository).findAllByOrganizerUser(any());
	}
	
	@Test
	public void testGetAllTasksByUserIdAndDateRange() {
		Task task1 = taskFactory.createOneTask();
		Task task2 = taskFactory.createOneTask();
		
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser());
		
		task1.setOrganizerUser(organizerUser);
		task2.setOrganizerUser(organizerUser);
		task1.setTaskPriority(taskPriorityFactory.createOneTaskPriority());
		task2.setTaskPriority(taskPriorityFactory.createOneTaskPriority());

		when(taskRepository.findAllByOrganizerUserAndTaskStartDateBetween(organizerUser, task1.getTaskStartDate().minusDays(1), task2.getTaskEndDate().plusDays(1))).thenReturn(java.util.List.of(task1, task2));

		java.util.List<Task> tasks = taskService.getAllTasksByUserIdAndDateRange(organizerUser, task1.getTaskStartDate().minusDays(1), task2.getTaskEndDate().plusDays(1));

		assertEquals(2, tasks.size());
		assertEquals(task1, tasks.get(0));
		assertEquals(task2, tasks.get(1));
	}
	
	@Test
	public void testGetAllTasksByOcurrenceUniqueId() {
		String uniqueId = "unique-id-123";
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser(appUserFactory.createOneAppUser());
		
		List<Task> taskList = new java.util.ArrayList<>();
		
		for (int i = 0; i < 5; i++) {
			Task task = taskFactory.createOneTask();
			task.setOcurrenceUniqueId(uniqueId);
			task.setOrganizerUser(organizerUser);
			task.setTaskPriority(taskPriorityFactory.createOneTaskPriority());
			
			taskList.add(task);
		}
		
		when(taskRepository.findAllByOcurrenceUniqueId(uniqueId)).thenReturn(taskList);
		taskList = taskService.getAllTasksByOcurrenceUniqueId(uniqueId);
		
		assertEquals(5, taskList.size());
	}
	
	
	
}
