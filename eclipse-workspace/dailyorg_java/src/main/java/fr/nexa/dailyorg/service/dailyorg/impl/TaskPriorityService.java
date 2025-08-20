package fr.nexa.dailyorg.service.dailyorg.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.dailyorg.TaskPriority;
import fr.nexa.dailyorg.repository.dailyorg.ITaskPriorityRepository;
import fr.nexa.dailyorg.service.dailyorg.ITaskPriorityService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class TaskPriorityService implements ITaskPriorityService {

	private List<TaskPriority> prioritiesListBuffer = new ArrayList<>();
	
	private final ITaskPriorityRepository taskPriorityRepository;

	@Override
	public Optional<TaskPriority> findTaskPriorityById(long taskPriorityId) {
		return taskPriorityRepository.findById(taskPriorityId);
	}
	
	@Override
	public TaskPriority createTaskPriority(String taskPriorityName) {
		TaskPriority taskPriority = TaskPriority.builder().taskPriorityName(taskPriorityName).build();
		return taskPriorityRepository.save(taskPriority);
	}

	@Override
	public Optional<TaskPriority> getTaskPriorityByTaskPriorityName(String taskPriorityName) {
		return taskPriorityRepository.findByTaskPriorityName(taskPriorityName);
	}

	@Override
	public List<TaskPriority> getAllTaskPriorities() {
		prioritiesListBuffer.clear();

		taskPriorityRepository.findAll().forEach(new Consumer<TaskPriority>() {
			@Override
			public void accept(TaskPriority taskPriority) {
				prioritiesListBuffer.add(taskPriority);
			}
		});

		return prioritiesListBuffer;
	}
}
