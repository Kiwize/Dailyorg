package fr.nexa.dailyorg.components.workout;

import java.util.List;

import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.model.workout.Muscle;
import fr.nexa.dailyorg.repository.workout.IMuscleRepository;
import jakarta.annotation.PostConstruct;

@Component
public class MuscleSeeder {

    private final IMuscleRepository muscleRepository;

    public MuscleSeeder(IMuscleRepository muscleRepository) {
        this.muscleRepository = muscleRepository;
    }

    @PostConstruct
    public void populateMuscles() {
        List<String> muscles = List.of(
            "Chest", "Back", "Shoulders", "Biceps", "Triceps",
            "Forearms", "Quadriceps", "Hamstrings", "Calves", "Glutes",
            "Abdominals", "Obliques", "Trapezius", "Lower Back"
        );

        muscles.forEach(muscleName -> {
            if (muscleRepository.findByName(muscleName).isEmpty()) {
                muscleRepository.save(Muscle.builder().name(muscleName).build());
            }
        });

        System.out.println("✅ Muscles populated successfully!");
    }
}
