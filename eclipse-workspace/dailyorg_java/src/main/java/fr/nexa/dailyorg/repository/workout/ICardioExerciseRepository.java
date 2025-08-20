package fr.nexa.dailyorg.repository.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg.model.workout.CardioExercise;

@Repository
public interface ICardioExerciseRepository extends JpaRepository<CardioExercise, Long>{

}
