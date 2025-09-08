package fr.nexa.dailyorg.service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.repository.IAppUserRepository;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class AppUserService implements IAppUserService {

	private final IAppUserRepository appUserRepository;
	
	@Override
	public AppUser addUser(AppUser appuser) throws Exception {
		return appUserRepository.save(appuser);
	}

	@Override
	public AppUser updateUser(AppUser appuser) throws Exception {
		return appUserRepository.save(appuser);
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
