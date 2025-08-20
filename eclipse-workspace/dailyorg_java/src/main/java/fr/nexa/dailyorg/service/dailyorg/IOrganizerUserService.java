package fr.nexa.dailyorg.service.dailyorg;

import java.util.Optional;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;

public interface IOrganizerUserService {
	
	Optional<OrganizerUser> findByAppUserId(long userId);
	OrganizerUser findByAppUserId(AppUser userId);
	OrganizerUser create(OrganizerUser organizerUser);
	OrganizerUser update(OrganizerUser organizerUser);
	void delete(OrganizerUser organizerUser);
	
}
