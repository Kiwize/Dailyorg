package fr.nexa.dailyorg.components.factory.dailyorg;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;

public class OrganizerUserFactory {
	
	public OrganizerUser createOneOrganizerUser(AppUser appUser) {
		return OrganizerUser.builder()
				.appUser(appUser)
				.calendarFirstShownHour(0)
				.calendarTotalShownHours(24)
				.exp_points(0)
				.build();
	}
}
