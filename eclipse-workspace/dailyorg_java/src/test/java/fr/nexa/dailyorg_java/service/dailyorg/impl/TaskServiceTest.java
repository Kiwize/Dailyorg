package fr.nexa.dailyorg_java.service.dailyorg.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.nexa.dailyorg_java.model.dailyorg.Task;
import fr.nexa.dailyorg_java.repository.dailyorg.ITaskRepository;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTest {
	
	@Mock
	private ITaskRepository taskRepository;
	
	@InjectMocks
	private TaskService taskService;
	
	@Test
	void testGetTaskByID() {
		Task task = Task.builder()
				.taskId(1L)
				.taskName("Test Task")
				.taskDescription("Test Description")
				.build();
		
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		
		Task result = taskService.getTaskById(1L);
		
		assertThat(result.getTaskName()).isEqualTo("Test Task");	
	}
	
	@Test
	void testAddTask() {
		Task task = Task.builder()
				.taskId(1L)
				.taskName("Test Task")
				.taskDescription("Test Description")
				.build();
		
		when(taskRepository.save(task)).thenReturn(task);
		
		Task result = taskService.addTask(task);
		
		assertThat(result.getTaskName()).isEqualTo("Test Task");
	}
	
	@Test
	void testUpdateTask() {
		Task task = Task.builder()
				.taskId(1L)
				.taskName("Test Task")
				.taskDescription("Test Description")
				.build();
		
		when(taskRepository.save(task)).thenReturn(task);
		
		Task result = taskService.updateTask(task);
		
		assertThat(result.getTaskName()).isEqualTo("Test Task");
	}
	
	@Test
	void testDeleteTask() {
		Task task = Task.builder()
				.taskId(1L)
				.taskName("Test Task")
				.taskDescription("Test Description")
				.build();
		
		when(taskRepository.findById(1L)).thenReturn(Optional.of(task));
		
		Task result = taskService.deleteTask(1L);
		
		assertThat(result.getTaskName()).isEqualTo("Test Task");
	}
	
	@Test
	void testGetAllTasksByUserId() {
		Task task = Task.builder()
				.taskId(1L)
				.taskName("Test Task")
				.taskDescription("Test Description")
				.build();
		
		when(taskRepository.findAllByOrganizerUser(task.getOrganizerUser())).thenReturn(List.of(task));
		
		List<Task> result = taskService.getAllTasksByUserId(task.getOrganizerUser());
		
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
	}
	
	@Test
	void testGetAllTasksByUserIdAndDate() {
		Task task = Task.builder()
				.taskId(1L)
				.taskName("Test Task")
				.taskDescription("Test Description")
				.build();
		
		when(taskRepository.findAllByOrganizerUserAndTaskStartDate(task.getOrganizerUser(), task.getTaskStartDate())).thenReturn(List.of(task));
		
		List<Task> result = taskService.getAllTasksByUserIdAndDate(task.getOrganizerUser(), task.getTaskStartDate());
		
		assertThat(result).hasSize(1);
		assertThat(result.get(0).getTaskName()).isEqualTo("Test Task");
	}
}
