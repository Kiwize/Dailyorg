package fr.nexa.dailyorg_java.service.dailyorg.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;

import fr.nexa.dailyorg_java.model.dailyorg.TaskOccurence;
import fr.nexa.dailyorg_java.repository.dailyorg.ITaskOccurenceRepository;
import fr.nexa.dailyorg_java.service.dailyorg.ITaskOccurenceService;

public class TaskOccurenceService implements ITaskOccurenceService {

	@Autowired
	private ITaskOccurenceRepository taskOccurenceRepository;
	
	@Override
	public Optional<TaskOccurence> findById(Long id) {
		return taskOccurenceRepository.findById(id);
	}

	@Override
	public TaskOccurence update(TaskOccurence taskOccurence) {
		return taskOccurenceRepository.save(taskOccurence);
	}

	@Override
	public TaskOccurence create(TaskOccurence taskOccurence) {
		return taskOccurenceRepository.save(taskOccurence);
	}

	@Override
	public void delete(TaskOccurence taskOccurence) {
		taskOccurenceRepository.delete(taskOccurence);
	}

}
