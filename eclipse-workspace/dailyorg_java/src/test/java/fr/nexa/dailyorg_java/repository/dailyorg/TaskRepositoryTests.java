package fr.nexa.dailyorg_java.repository.dailyorg;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.model.dailyorg.Task;

@DataJpaTest
public class TaskRepositoryTests {

	@Autowired
	private ITaskRepository taskRepository;
	
	@Autowired
	private IOrganizerUserRepository organizerUserRepository;

	@Test
	void findAllByOrganizerUser_shouldReturnTasksForOrganizer() {
		OrganizerUser organizer = OrganizerUser.builder().energy_profile("Normal").exp_points(0).level(1).build();
		
		organizerUserRepository.save(organizer);

		Task task1 = Task.builder().taskName("Task A").taskStartDate(LocalDateTime.of(2025, 6, 13, 10, 0))
				.taskEndDate(LocalDateTime.of(2025, 6, 13, 11, 0)).organizerUser(organizer).build();

		Task task2 = Task.builder().taskName("Task B").taskStartDate(LocalDateTime.of(2025, 6, 13, 12, 0))
				.taskEndDate(LocalDateTime.of(2025, 6, 13, 13, 0)).organizerUser(organizer).build();

		taskRepository.saveAll(List.of(task1, task2));

		List<Task> tasks = taskRepository.findAllByOrganizerUser(organizer);

		assertThat(tasks).hasSize(2);
		assertThat(tasks).extracting(Task::getTaskName).containsExactlyInAnyOrder("Task A", "Task B");
	}

	@Test
	void findAllByOrganizerUserAndTaskStartDate_shouldFilterCorrectly() {
		OrganizerUser organizer = OrganizerUser.builder().energy_profile("Normal").exp_points(0).level(1).build();
		
		organizerUserRepository.save(organizer);

		LocalDateTime dateFilter = LocalDateTime.of(2025, 6, 13, 10, 0);

		Task matchTask = Task.builder().taskName("Matching Task").taskStartDate(dateFilter)
				.taskEndDate(dateFilter.plusHours(1)).organizerUser(organizer).build();

		Task nonMatchingTask = Task.builder().taskName("Other Task").taskStartDate(LocalDateTime.of(2025, 6, 14, 10, 0))
				.taskEndDate(LocalDateTime.of(2025, 6, 14, 11, 0)).organizerUser(organizer).build();

		taskRepository.saveAll(List.of(matchTask, nonMatchingTask));

		List<Task> tasks = taskRepository.findAllByOrganizerUserAndTaskStartDate(organizer, dateFilter);

		assertThat(tasks).hasSize(1);
		assertThat(tasks.get(0).getTaskName()).isEqualTo("Matching Task");
	}
}
