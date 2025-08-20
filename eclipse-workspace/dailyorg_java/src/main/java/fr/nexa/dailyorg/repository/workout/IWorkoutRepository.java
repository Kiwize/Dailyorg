package fr.nexa.dailyorg.repository.workout;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.workout.WorkoutSession;

@Repository
public interface IWorkoutRepository extends JpaRepository<WorkoutSession, Long>{

	List<WorkoutSession> findByUserId(AppUser userId);
	
}
