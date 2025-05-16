package fr.nexa.dailyorg_java.service.dailyorg;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;

public interface IOrganizerUserService {
	
	OrganizerUser findByAppUserId(AppUser userId);
	OrganizerUser create(OrganizerUser organizerUser);
	OrganizerUser update(OrganizerUser organizerUser);
	void delete(OrganizerUser organizerUser);
	
}
