package fr.nexa.dailyorg_java.repository.dailyorg;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.dailyorg.TaskPriority;

@Repository
public interface ITaskPriorityRepository extends CrudRepository<TaskPriority, Long>{
	
	Optional<TaskPriority> findByTaskPriorityName(String name);

}
