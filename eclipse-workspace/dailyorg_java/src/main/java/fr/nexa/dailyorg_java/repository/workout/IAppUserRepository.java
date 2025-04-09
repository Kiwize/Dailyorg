package fr.nexa.dailyorg_java.repository.workout;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.workout.AppUser;

@Repository
public interface IAppUserRepository extends JpaRepository<AppUser, Long>{

	Optional<AppUser> findByEmail(String email);
	
}
