package fr.nexa.dailyorg_java.repository.workout;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.workout.Exercise;
import fr.nexa.dailyorg_java.model.workout.WorkoutRecord;
import jakarta.transaction.Transactional;

@Repository
public interface IWorkoutRecordRepository extends JpaRepository<WorkoutRecord, Long> {
	
	@Query("SELECT wr.exerciseId FROM WorkoutRecord wr WHERE wr.workoutSession.id = :sessionId")
	List<Exercise> findExercisesByWorkoutSession(@Param("sessionId") Long sessionId);
	
	@Query("SELECT wr FROM WorkoutRecord wr WHERE wr.workoutSession.id = :sessionId")
	List<WorkoutRecord> findByWorkoutSession(@Param("sessionId") Long sessionId);
	
	@Modifying
    @Transactional
    @Query("DELETE FROM WorkoutRecord wr WHERE wr.workoutSession.id = :workoutSessionId AND wr.exerciseId.id = :exerciseId")
    void deleteByWorkoutSessionAndExercise(@Param("workoutSessionId") Long workoutSessionId, @Param("exerciseId") Long exerciseId);
}
