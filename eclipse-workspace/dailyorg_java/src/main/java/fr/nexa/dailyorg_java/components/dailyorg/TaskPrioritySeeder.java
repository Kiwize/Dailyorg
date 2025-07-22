package fr.nexa.dailyorg_java.components.dailyorg;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.nexa.dailyorg_java.model.dailyorg.TaskPriority;
import fr.nexa.dailyorg_java.repository.dailyorg.ITaskPriorityRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TaskPrioritySeeder {

    private final ITaskPriorityRepository taskPriorityRepository;

    @PostConstruct
    public void populateMuscles() {
        List<String> priorities = List.of(
            "Low", "Medium", "High", "Urgent"
        );

        priorities.forEach(priority -> {
            if (taskPriorityRepository.findByTaskPriorityName(priority).isEmpty()) {
            	taskPriorityRepository.save(TaskPriority.builder().taskPriorityName(priority).build());
            }
        });

        System.out.println("✅ Task priorities populated successfully!");
    }
}
