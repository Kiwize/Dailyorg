package fr.nexa.dailyorg.components.dailyorg;

import java.util.Arrays;

import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.model.dailyorg.TaskPriority;
import fr.nexa.dailyorg.repository.dailyorg.ITaskPriorityRepository;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class TaskPrioritySeeder {

    private final ITaskPriorityRepository taskPriorityRepository;

    @PostConstruct
    public void populateTaskPriorities() {
        Arrays.asList(ETaskPriorities.values()).forEach(priority -> {
            if (taskPriorityRepository.findByTaskPriorityName(priority.getPriorityName()).isEmpty()) {
            	taskPriorityRepository.save(TaskPriority.builder().taskPriorityName(priority.getPriorityName()).taskPriorityLevel(priority.getPriorityLevel()).build());
            }
        });

        System.out.println("✅ Task priorities populated successfully!");
    }
}
