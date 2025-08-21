package fr.nexa.dailyorg.components.factory.dailyorg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.components.factory.AppUserFactory;
import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.repository.dailyorg.IOrganizerUserRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class OrganizerUserFactory {
	
	private final AppUserFactory appUserFactory;
	
	private final IOrganizerUserRepository organizerUserRepository;

	
	public OrganizerUser createOneOrganizerUser() {
		AppUser appUser = appUserFactory.createAndInsertOneAppUser();
		
		return OrganizerUser.builder()
				.appUser(appUser)
				.calendarFirstShownHour(0)
				.calendarTotalShownHours(24)
				.exp_points(0)
				.build();
	}
	
	public OrganizerUser createAndInsertOneOrganizerUser() {
		OrganizerUser organizerUser = createOneOrganizerUser();
		return organizerUserRepository.save(organizerUser);
	}
}
