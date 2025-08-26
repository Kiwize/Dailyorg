package fr.nexa.dailyorg.service.dailyorg.impl;

import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.dailyorg.TaskPriority;
import fr.nexa.dailyorg.repository.dailyorg.ITaskPriorityRepository;
import fr.nexa.dailyorg.service.dailyorg.ITaskPriorityService;
import fr.nexa.dailyorg.utils.EErrorMessages;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TaskPriorityService implements ITaskPriorityService {

	private final ITaskPriorityRepository taskPriorityRepository;

	@Override
	public Optional<TaskPriority> findTaskPriorityById(Long taskPriorityId) {
		if (taskPriorityId == null) {
			return Optional.empty();
		}
		return taskPriorityRepository.findById(taskPriorityId);
	}

	@Override
	public TaskPriority createTaskPriority(TaskPriority taskPriority) {
		if (taskPriority == null) {
			Logger.getLogger(this.getClass().getName()).severe(EErrorMessages.NULL_VALUE.getMessage());
			throw new IllegalArgumentException(EErrorMessages.NULL_VALUE.getMessage());
		}
		return taskPriorityRepository.save(taskPriority);
	}

	@Override
	public Optional<TaskPriority> getTaskPriorityByTaskPriorityName(String taskPriorityName) {
		if (taskPriorityName == null) {
			return Optional.empty();
		}
		return taskPriorityRepository.findByTaskPriorityName(taskPriorityName);
	}

	@Override
	public List<TaskPriority> getAllTaskPriorities() {
		return StreamSupport.stream(taskPriorityRepository.findAll().spliterator(), false).toList();
	}
}