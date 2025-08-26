package fr.nexa.dailyorg.service.dailyorg.impl;

import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.repository.dailyorg.IOrganizerUserRepository;
import fr.nexa.dailyorg.service.dailyorg.IOrganizerUserService;
import fr.nexa.dailyorg.utils.EErrorMessages;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class OrganizerUserService implements IOrganizerUserService{

	private final IOrganizerUserRepository organizerUserRepository;
	
	@Override
	public Optional<OrganizerUser> findByAppUserId(Long userId) {
		if(userId == null) {
			return Optional.empty();
		}
		return organizerUserRepository.findById(userId);
	}
	
	@Override
	public OrganizerUser create(OrganizerUser organizerUser) {
		if(organizerUser == null) {
			Logger.getLogger(this.getClass().getName()).severe(EErrorMessages.NULL_VALUE.getMessage());
			throw new IllegalArgumentException(EErrorMessages.NULL_VALUE.getMessage());
		}
		return organizerUserRepository.save(organizerUser);
	}
	
	@Override
	public void delete(OrganizerUser organizerUser) {
		if(organizerUser == null) {
			Logger.getLogger(this.getClass().getName()).severe(EErrorMessages.NULL_VALUE.getMessage());
			throw new IllegalArgumentException(EErrorMessages.NULL_VALUE.getMessage());
		}
		organizerUserRepository.delete(organizerUser);
	}
	
	@Override
	public OrganizerUser update(OrganizerUser organizerUser) {
		if(organizerUser == null) {
			Logger.getLogger(this.getClass().getName()).severe(EErrorMessages.NULL_VALUE.getMessage());
			throw new IllegalArgumentException(EErrorMessages.NULL_VALUE.getMessage());
		}
		return organizerUserRepository.save(organizerUser);
	}

}
