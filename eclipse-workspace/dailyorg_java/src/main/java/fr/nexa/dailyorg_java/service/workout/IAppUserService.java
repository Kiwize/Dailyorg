package fr.nexa.dailyorg_java.service.workout;

import java.util.Optional;

import fr.nexa.dailyorg_java.model.workout.AppUser;

public interface IAppUserService {

	AppUser addUser(AppUser appuser) throws Exception;
	void updateUser(AppUser appuser) throws Exception;
	void removeUser(AppUser appuser) throws Exception;
	AppUser getAppUserByID(long userID) throws Exception;
	Optional<AppUser> findByEmail(String email) throws Exception;
	
}
