package fr.nexa.dailyorg_java.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.WorkoutSession;

@Repository
public interface IWorkoutRepository extends JpaRepository<WorkoutSession, Long>{

	List<WorkoutSession> findByUserId(AppUser userId);
	
}
