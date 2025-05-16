package fr.nexa.dailyorg_java.repository.dailyorg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.dailyorg.TaskOccurence;

@Repository
public interface ITaskOccurenceRepository extends JpaRepository<TaskOccurence, Long> {

}
