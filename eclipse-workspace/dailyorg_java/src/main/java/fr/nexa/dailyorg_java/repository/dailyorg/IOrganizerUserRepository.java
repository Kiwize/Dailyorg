package fr.nexa.dailyorg_java.repository.dailyorg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;

@Repository
public interface IOrganizerUserRepository extends JpaRepository<OrganizerUser, Long> {
	
	OrganizerUser findByAppUser(AppUser appUser);
}
