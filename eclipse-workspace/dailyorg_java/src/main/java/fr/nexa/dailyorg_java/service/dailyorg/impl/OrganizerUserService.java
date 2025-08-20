package fr.nexa.dailyorg_java.service.dailyorg.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.repository.dailyorg.IOrganizerUserRepository;
import fr.nexa.dailyorg_java.service.dailyorg.IOrganizerUserService;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizerUserService implements IOrganizerUserService{

	private final IOrganizerUserRepository organizerUserRepository;
	
	@Override
	public OrganizerUser findByAppUserId(AppUser userId) {
		return organizerUserRepository.findByAppUser(userId);
	}
	
	@Override
	public Optional<OrganizerUser> findByAppUserId(long userId) {
		return organizerUserRepository.findById(userId);
	}
	
	@Override
	public OrganizerUser create(OrganizerUser organizerUser) {
		return organizerUserRepository.save(organizerUser);
	}
	
	@Override
	public void delete(OrganizerUser organizerUser) {
		organizerUserRepository.delete(organizerUser);
	}
	
	@Override
	public OrganizerUser update(OrganizerUser organizerUser) {
		return organizerUserRepository.save(organizerUser);
	}

}
