package fr.nexa.dailyorg.service;

import java.util.Optional;

import fr.nexa.dailyorg.model.AppUser;

public interface IAppUserService {

	AppUser addUser(AppUser appuser) throws Exception;
	AppUser updateUser(AppUser appuser) throws Exception;
	void removeUser(AppUser appuser) throws Exception;
	AppUser getAppUserByID(long userID) throws Exception;
	Optional<AppUser> findByEmail(String email) throws Exception;
	
}
