package fr.nexa.dailyorg_java.service.dailyorg.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.dailyorg.TaskOccurrence;
import fr.nexa.dailyorg_java.repository.dailyorg.ITaskOccurenceRepository;
import fr.nexa.dailyorg_java.service.dailyorg.ITaskOccurenceService;

@Service
public class TaskOccurrenceService implements ITaskOccurenceService {

	@Autowired
	private ITaskOccurenceRepository taskOccurenceRepository;
	
	@Override
	public Optional<TaskOccurrence> findById(Long id) {
		return taskOccurenceRepository.findById(id);
	}

	@Override
	public TaskOccurrence update(TaskOccurrence taskOccurence) {
		return taskOccurenceRepository.save(taskOccurence);
	}

	@Override
	public TaskOccurrence create(TaskOccurrence taskOccurence) {
		return taskOccurenceRepository.save(taskOccurence);
	}

	@Override
	public void delete(TaskOccurrence taskOccurence) {
		taskOccurenceRepository.delete(taskOccurence);
	}

}
