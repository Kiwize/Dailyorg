package fr.nexa.dailyorg_java.service.dailyorg;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.dailyorg.TaskOccurence;

public interface ITaskOccurenceService {
	
	Optional<TaskOccurence> findById(Long id);
	TaskOccurence update(TaskOccurence taskOccurence);
	TaskOccurence create(TaskOccurence taskOccurence);
	void delete(TaskOccurence taskOccurence);

}
