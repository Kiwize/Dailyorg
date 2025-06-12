package fr.nexa.dailyorg_java.repository.dailyorg;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import fr.nexa.dailyorg_java.model.dailyorg.Task;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;

@DataJpaTest
public class TaskRepositoryTest {

    @Autowired
    private ITaskRepository taskRepository;

    @Test
    void testSaveAndFindById() {
        OrganizerUser user = OrganizerUser.builder().energy_profile("A").level(0).exp_points(0).build();
        Task task = Task.builder()
                .taskName("Test")
                .taskRequiredEnergy(1)
                .taskStartDate(LocalDateTime.now())
                .taskEndDate(LocalDateTime.now())
                .organizerUser(user)
                .build();
        task = taskRepository.save(task);
        assertThat(task.getTaskId()).isGreaterThan(0);

        Task found = taskRepository.findById(task.getTaskId()).orElse(null);
        assertThat(found).isNotNull();
        assertThat(found.getTaskName()).isEqualTo("Test");
    }
}
