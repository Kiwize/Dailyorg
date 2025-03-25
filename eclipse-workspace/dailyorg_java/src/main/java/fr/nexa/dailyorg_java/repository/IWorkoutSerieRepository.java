package fr.nexa.dailyorg_java.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.WorkoutSerie;

@Repository
public interface IWorkoutSerieRepository extends JpaRepository<WorkoutSerie, Long>{

}
