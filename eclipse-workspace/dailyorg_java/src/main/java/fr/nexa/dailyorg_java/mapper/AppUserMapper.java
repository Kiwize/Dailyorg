package fr.nexa.dailyorg_java.mapper;

import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.DTO.AppUserDTO;
import fr.nexa.dailyorg_java.model.AppUser;

@Service
public class AppUserMapper {

	public AppUserDTO toDTO(AppUser appUser) {
		if (appUser == null) {
			return null;
		}
		return new AppUserDTO(appUser.getSurname(), appUser.getUsername(), appUser.getEmail(), appUser.getPassword(),
				appUser.getProfilepicturelink());
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
