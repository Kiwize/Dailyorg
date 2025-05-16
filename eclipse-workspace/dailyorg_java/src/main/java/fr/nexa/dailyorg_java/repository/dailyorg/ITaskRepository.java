package fr.nexa.dailyorg_java.repository.dailyorg;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;

@Repository
public interface ITaskRepository extends JpaRepository<Task, Long> {

	List<Task> findAllByOrganizerUser(OrganizerUser organizerUser);
	List<Task> findAllByOrganizerUserAndTaskStartDate(OrganizerUser organizerUser, LocalDateTime taskStartDate);
	
}
