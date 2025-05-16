package fr.nexa.dailyorg_java.service.dailyorg.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.repository.dailyorg.IOrganizerUserRepository;
import fr.nexa.dailyorg_java.service.dailyorg.IOrganizerUserService;

@Service
public class OrganizerUserService implements IOrganizerUserService{

	@Autowired
	private IOrganizerUserRepository organizerUserRepository;
	
	@Override
	public OrganizerUser findByAppUserId(AppUser userId) {
		return organizerUserRepository.findByAppUser(userId);
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
