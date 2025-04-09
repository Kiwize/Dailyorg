package fr.nexa.dailyorg_java.repository.workout;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.workout.Muscle;

@Repository
public interface IMuscleRepository extends JpaRepository<Muscle, Long> {
    Optional<Muscle> findByName(String name);
}
