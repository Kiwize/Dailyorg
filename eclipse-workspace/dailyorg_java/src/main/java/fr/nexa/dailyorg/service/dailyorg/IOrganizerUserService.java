package fr.nexa.dailyorg.service.dailyorg;

import java.util.Optional;

import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;

public interface IOrganizerUserService {
	
	Optional<OrganizerUser> findByAppUserId(Long userId);
	OrganizerUser create(OrganizerUser organizerUser);
	OrganizerUser update(OrganizerUser organizerUser);
	void delete(OrganizerUser organizerUser);
	
}
