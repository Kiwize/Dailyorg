package fr.nexa.dailyorg.mapper.dailyorg;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.DTO.dailyorg.TaskDTO;
import fr.nexa.dailyorg.model.dailyorg.Task;

@Service
public class TaskMapper {

	// Method to convert Task entity to TaskDTO
	public TaskDTO toDTO(Task task) {
		if (task == null) {
			return null;
		}
		TaskDTO dto = new TaskDTO();
		dto.setTaskId(task.getId());
		dto.setTaskName(task.getTaskName());
		dto.setTaskDescription(task.getTaskDescription());
		dto.setTaskCreationDate(task.getTaskCreationDate().toString());
		dto.setTaskCompletionDate(
				task.getTaskCompletionDate() != null ? task.getTaskCompletionDate().toString() : null);
		dto.setTaskRequiredEnergy(task.getTaskRequiredEnergy());
		dto.setTaskStartDate(task.getTaskStartDate());
		dto.setTaskEndDate(task.getTaskEndDate());
		return dto;
	}

	// Method to convert TaskDTO to Task entity
	public Task toEntity(TaskDTO dto) {
		if (dto == null) {
			return null;
		}
		Task task = new Task();
		task.setId(dto.getTaskId());
		task.setTaskName(dto.getTaskName());
		task.setTaskDescription(dto.getTaskDescription());
		task.setTaskCreationDate(LocalDateTime.parse(dto.getTaskCreationDate()));
		task.setTaskCompletionDate(
				dto.getTaskCompletionDate() != null ? LocalDateTime.parse(dto.getTaskCompletionDate()) : null);
		task.setTaskRequiredEnergy(dto.getTaskRequiredEnergy());
		task.setTaskStartDate(dto.getTaskStartDate());
		task.setTaskEndDate(dto.getTaskEndDate());
		return task;
	}

	// Convert a List of Task entities to a List of TaskDTOs
	public List<TaskDTO> toDTOList(List<Task> tasks) {
		if (tasks == null) {
			return null;
		}
		return tasks.stream().map(this::toDTO).collect(Collectors.toList());
	}

	// Convert a List of TaskDTOs to a List of Task entities
	public List<Task> toEntityList(List<TaskDTO> dtos) {
		if (dtos == null) {
			return null;
		}
		return dtos.stream().map(this::toEntity).collect(Collectors.toList());
	}

}
