package fr.nexa.dailyorg_java.service.dailyorg;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.dailyorg.TaskOccurrence;

public interface ITaskOccurenceService {
	
	Optional<TaskOccurrence> findById(Long id);
	TaskOccurrence update(TaskOccurrence taskOccurence);
	TaskOccurrence create(TaskOccurrence taskOccurence);
	void delete(TaskOccurrence taskOccurence);

}
