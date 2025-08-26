package fr.nexa.dailyorg.service.dailyorg.impl;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.nexa.dailyorg.components.factory.dailyorg.TaskPriorityFactory;
import fr.nexa.dailyorg.model.dailyorg.TaskPriority;
import fr.nexa.dailyorg.repository.dailyorg.ITaskPriorityRepository;
import fr.nexa.dailyorg.utils.EErrorMessages;

@ExtendWith(MockitoExtension.class)
public class TaskPriorityServiceTest {

	@InjectMocks
	private TaskPriorityService taskPriorityService;

	@Mock
	private ITaskPriorityRepository taskPriorityRepository;

	// Factories
	private final TaskPriorityFactory taskPriorityFactory = new TaskPriorityFactory();

	@Test
	void testFindTaskPriorityById() {
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority();

		when(taskPriorityRepository.findById(1L)).thenReturn(Optional.of(taskPriority));

		Optional<TaskPriority> found = taskPriorityService.findTaskPriorityById(1L);

		assertEquals(taskPriority, found.get());
		assertTrue(found.isPresent());
	}
	
	@Test
	void testFindTaskPriorityById_null() {
		Optional<TaskPriority> found = taskPriorityService.findTaskPriorityById(null);
		assertTrue(found.isEmpty());
	}

	@Test
	void testFindTaskPriorityById_notFound() {
		when(taskPriorityRepository.findById(1L)).thenReturn(Optional.empty());

		Optional<TaskPriority> found = taskPriorityService.findTaskPriorityById(1L);

		assertTrue(found.isEmpty());
	}

	@Test
	void testGetTaskPriorityByTaskPriorityName() {
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority();

		when(taskPriorityRepository.findByTaskPriorityName("High")).thenReturn(Optional.of(taskPriority));

		Optional<TaskPriority> found = taskPriorityService.getTaskPriorityByTaskPriorityName("High");

		assertEquals(taskPriority, found.get());
		assertTrue(found.isPresent());
	}

	@Test
	void testGetTaskPriorityByTaskPriorityName_notFound() {
		when(taskPriorityRepository.findByTaskPriorityName("High")).thenReturn(Optional.empty());

		Optional<TaskPriority> found = taskPriorityService.getTaskPriorityByTaskPriorityName("High");

		assertTrue(found.isEmpty());
	}
	
	@Test
	void testGetTaskPriorityByTaskPriorityName_null() {
		Optional<TaskPriority> found = taskPriorityService.getTaskPriorityByTaskPriorityName(null);
		assertTrue(found.isEmpty());
	}

	@Test
	void testGetAllTaskPriorities() {
		TaskPriority taskPriority1 = taskPriorityFactory.createOneTaskPriority();
		TaskPriority taskPriority2 = taskPriorityFactory.createOneTaskPriority();

		when(taskPriorityRepository.findAll()).thenReturn(List.of(taskPriority1, taskPriority2));

		List<TaskPriority> found = taskPriorityService.getAllTaskPriorities();

		assertEquals(2, found.size());
		assertTrue(found.contains(taskPriority1));
		assertTrue(found.contains(taskPriority2));
	}

	@Test
	void testCreateTaskPriority() {
		TaskPriority taskPriority = taskPriorityFactory.createOneTaskPriority();

		when(taskPriorityRepository.save(taskPriority)).thenReturn(taskPriority);

		TaskPriority created = taskPriorityService.createTaskPriority(taskPriority);

		assertEquals(taskPriority, created);
	}

	@Test
	void testCreateNullTaskPriority() {
		assertThatIllegalArgumentException().isThrownBy(() -> taskPriorityService.createTaskPriority(null)).withMessage(EErrorMessages.NULL_VALUE.getMessage());
	}

}
