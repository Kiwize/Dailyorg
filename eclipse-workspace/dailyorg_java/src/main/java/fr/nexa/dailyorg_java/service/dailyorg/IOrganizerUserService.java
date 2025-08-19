package fr.nexa.dailyorg_java.service.dailyorg;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;

public interface IOrganizerUserService {
	
	Optional<OrganizerUser> findByAppUserId(long userId);
	OrganizerUser findByAppUserId(AppUser userId);
	OrganizerUser create(OrganizerUser organizerUser);
	OrganizerUser update(OrganizerUser organizerUser);
	void delete(OrganizerUser organizerUser);
	
}
