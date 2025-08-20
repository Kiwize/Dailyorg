package fr.nexa.dailyorg.mapper.dailyorg;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.nexa.dailyorg.DTO.dailyorg.TaskDTO;
import fr.nexa.dailyorg.model.dailyorg.Task;

class TaskMapperTests {

	private TaskMapper taskMapper;

	@BeforeEach
	void setUp() {
		taskMapper = new TaskMapper();
	}

	@Test
	void testToDTO_withValidTask_shouldMapCorrectly() {
		Task task = new Task();
		task.setId(1L);
		task.setTaskName("Test Task");
		task.setTaskDescription("Description");
		task.setTaskCreationDate(LocalDateTime.of(2023, 1, 1, 12, 0));
		task.setTaskCompletionDate(LocalDateTime.of(2023, 1, 2, 14, 30));
		task.setTaskRequiredEnergy(100);
		task.setTaskStartDate(LocalDateTime.of(2023, 1, 1, 13, 0));
		task.setTaskEndDate(LocalDateTime.of(2023, 1, 1, 15, 0));

		TaskDTO dto = taskMapper.toDTO(task);

		assertNotNull(dto);
		assertEquals(task.getId(), dto.getTaskId());
		assertEquals(task.getTaskName(), dto.getTaskName());
		assertEquals(task.getTaskDescription(), dto.getTaskDescription());
		assertEquals("2023-01-01T12:00", dto.getTaskCreationDate());
		assertEquals("2023-01-02T14:30", dto.getTaskCompletionDate());
		assertEquals(task.getTaskRequiredEnergy(), dto.getTaskRequiredEnergy());
		assertEquals(task.getTaskStartDate(), dto.getTaskStartDate());
		assertEquals(task.getTaskEndDate(), dto.getTaskEndDate());
	}

	@Test
	void testToDTO_withNullTask_shouldReturnNull() {
		assertNull(taskMapper.toDTO(null));
	}

	@Test
	void testToEntity_withValidDTO_shouldMapCorrectly() {
		TaskDTO dto = new TaskDTO();
		dto.setTaskId(2L);
		dto.setTaskName("Another Task");
		dto.setTaskDescription("Some description");
		dto.setTaskCreationDate("2023-02-01T10:00");
		dto.setTaskCompletionDate("2023-02-03T12:00");
		dto.setTaskRequiredEnergy(200);
		dto.setTaskStartDate(LocalDateTime.of(2023, 2, 1, 10, 30));
		dto.setTaskEndDate(LocalDateTime.of(2023, 2, 1, 12, 30));

		Task task = taskMapper.toEntity(dto);

		assertNotNull(task);
		assertEquals(dto.getTaskId(), task.getId());
		assertEquals(dto.getTaskName(), task.getTaskName());
		assertEquals(dto.getTaskDescription(), task.getTaskDescription());
		assertEquals(LocalDateTime.parse(dto.getTaskCreationDate()), task.getTaskCreationDate());
		assertEquals(LocalDateTime.parse(dto.getTaskCompletionDate()), task.getTaskCompletionDate());
		assertEquals(dto.getTaskRequiredEnergy(), task.getTaskRequiredEnergy());
		assertEquals(dto.getTaskStartDate(), task.getTaskStartDate());
		assertEquals(dto.getTaskEndDate(), task.getTaskEndDate());
	}

	@Test
	void testToEntity_withNullDTO_shouldReturnNull() {
		assertNull(taskMapper.toEntity(null));
	}

	@Test
	void testToDTOList_withValidList_shouldConvertAll() {
		Task task1 = new Task();
		task1.setId(1L);
		task1.setTaskName("Task 1");
		task1.setTaskDescription("Desc 1");
		task1.setTaskCreationDate(LocalDateTime.now());
		task1.setTaskRequiredEnergy(10);

		Task task2 = new Task();
		task2.setId(2L);
		task2.setTaskName("Task 2");
		task2.setTaskDescription("Desc 2");
		task2.setTaskCreationDate(LocalDateTime.now());
		task2.setTaskRequiredEnergy(20);

		List<TaskDTO> dtoList = taskMapper.toDTOList(List.of(task1, task2));

		assertNotNull(dtoList);
		assertEquals(2, dtoList.size());
		assertEquals("Task 1", dtoList.get(0).getTaskName());
		assertEquals("Task 2", dtoList.get(1).getTaskName());
	}

	@Test
	void testToDTOList_withNull_shouldReturnNull() {
		assertNull(taskMapper.toDTOList(null));
	}

	@Test
	void testToEntityList_withValidList_shouldConvertAll() {
		TaskDTO dto1 = new TaskDTO();
		dto1.setTaskId(1L);
		dto1.setTaskName("DTO 1");
		dto1.setTaskDescription("D1");
		dto1.setTaskCreationDate("2023-03-01T09:00");
		dto1.setTaskRequiredEnergy(50);

		TaskDTO dto2 = new TaskDTO();
		dto2.setTaskId(2L);
		dto2.setTaskName("DTO 2");
		dto2.setTaskDescription("D2");
		dto2.setTaskCreationDate("2023-03-02T09:00");
		dto2.setTaskRequiredEnergy(70);

		List<Task> taskList = taskMapper.toEntityList(List.of(dto1, dto2));

		assertNotNull(taskList);
		assertEquals(2, taskList.size());
		assertEquals("DTO 1", taskList.get(0).getTaskName());
		assertEquals("DTO 2", taskList.get(1).getTaskName());
	}

	@Test
	void testToEntityList_withNull_shouldReturnNull() {
		assertNull(taskMapper.toEntityList(null));
	}
}
