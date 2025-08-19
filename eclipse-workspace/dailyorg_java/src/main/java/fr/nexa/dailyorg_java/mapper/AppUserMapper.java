package fr.nexa.dailyorg_java.mapper;

import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.DTO.AppUserDTO;
import fr.nexa.dailyorg_java.DTO.dailyorg.OrganizerUserDTO;
import fr.nexa.dailyorg_java.model.AppUser;

@Service
public class AppUserMapper {

	public AppUserDTO toDTO(AppUser appUser) {
		if (appUser == null) {
			return null;
		}
		
		AppUserDTO appUserDTO = new AppUserDTO();
		appUserDTO.setSurname(appUser.getSurname());
		appUserDTO.setUsername(appUser.getUsername());
		appUserDTO.setEmail(appUser.getEmail());
		appUserDTO.setPassword(appUser.getPassword());
		appUserDTO.setProfilePictureLink(appUser.getProfilepicturelink());
		// If OrganizerUser is not null, you can map it here as well
		if (appUser.getOrganizerUser() != null) {
			OrganizerUserDTO organizerUserDTO = new OrganizerUserDTO();
			organizerUserDTO.setOrganizerUserId(appUser.getOrganizerUser().getOrganizerUserId());
			organizerUserDTO.setExperiencePoints(appUser.getOrganizerUser().getExp_points());
			organizerUserDTO.setCalendarFirstShownHour(appUser.getOrganizerUser().getCalendarFirstShownHour());
			organizerUserDTO.setCalendarTotalShownHours(appUser.getOrganizerUser().getCalendarTotalShownHours());
			
			appUserDTO.setOrganizerUser(organizerUserDTO);
		} else {
			appUserDTO.setOrganizerUser(null);
		}
		
		return appUserDTO;
	}

	public AppUser toEntity(AppUserDTO appUserDTO) {
		if (appUserDTO == null) {
			return null;
		}
		return AppUser.builder().surname(appUserDTO.getSurname()).username(appUserDTO.getUsername())
				.email(appUserDTO.getEmail()).password(appUserDTO.getPassword())
				.profilepicturelink(appUserDTO.getProfilePictureLink()).build();
	}
}
