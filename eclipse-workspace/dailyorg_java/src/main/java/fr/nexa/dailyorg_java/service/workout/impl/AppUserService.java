package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.repository.workout.IAppUserRepository;
import fr.nexa.dailyorg_java.service.workout.IAppUserService;

@Service
public class AppUserService implements IAppUserService {

	@Autowired
	private IAppUserRepository appUserRepository;
	
	@Override
	public AppUser addUser(AppUser appuser) throws Exception {
		return appUserRepository.save(appuser);
	}

	@Override
	public void updateUser(AppUser appuser) throws Exception {
		appUserRepository.save(appuser);
	}

	@Override
	public void removeUser(AppUser appuser) throws Exception {
		appUserRepository.delete(appuser);
	}

	@Override
	public AppUser getAppUserByID(long userID) throws Exception {
		return appUserRepository.getReferenceById(userID); 
	}

	
	@Override
	public Optional<AppUser> findByEmail(String email) throws Exception {
		return appUserRepository.findByEmail(email);
	}

}
