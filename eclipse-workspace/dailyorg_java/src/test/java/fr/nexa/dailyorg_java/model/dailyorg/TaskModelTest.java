package fr.nexa.dailyorg_java.model.dailyorg;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

public class TaskModelTest {

    @Test
    void testPrePersistSetsCreationDate() {
        Task task = Task.builder()
                .taskName("Test")
                .taskRequiredEnergy(1)
                .taskStartDate(LocalDateTime.now())
                .taskEndDate(LocalDateTime.now())
                .build();

        // creation date should be null before
        assertThat(task.getTaskCreationDate()).isNull();
        task.prePersist();
        assertThat(task.getTaskCreationDate()).isNotNull();
    }
}
