package fr.nexa.dailyorg.repository.dailyorg;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;

@Repository
public interface IOrganizerUserRepository extends JpaRepository<OrganizerUser, Long> {
	
	OrganizerUser findByAppUser(AppUser appUser);
}
