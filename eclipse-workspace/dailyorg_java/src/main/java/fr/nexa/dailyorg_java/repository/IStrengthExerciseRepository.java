package fr.nexa.dailyorg_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.StrengthExercise;

@Repository
public interface IStrengthExerciseRepository extends JpaRepository<StrengthExercise, Long>{

	 @Query("SELECT s FROM StrengthExercise s JOIN s.muscles m WHERE m.id = :muscleId")
	 List<StrengthExercise> findByMuscleId(@Param("muscleId") Long muscleId);
}
