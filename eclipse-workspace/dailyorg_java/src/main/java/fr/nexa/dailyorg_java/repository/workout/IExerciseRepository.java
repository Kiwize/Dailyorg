package fr.nexa.dailyorg_java.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.workout.Exercise;

@Repository
public interface IExerciseRepository extends JpaRepository<Exercise, Long>{

}
