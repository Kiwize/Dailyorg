package fr.nexa.dailyorg.dto.dailyorg;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrganizerUserDTO {

	private long organizerUserId;
	private int experiencePoints;
	private int calendarFirstShownHour;
	private int calendarTotalShownHours;
	
	
}
