package fr.nexa.dailyorg.service.dailyorg.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.nexa.dailyorg.components.factory.dailyorg.OrganizerUserFactory;
import fr.nexa.dailyorg.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg.repository.dailyorg.IOrganizerUserRepository;
import fr.nexa.dailyorg.utils.EErrorMessages;

@ExtendWith(MockitoExtension.class)
public class OrganizerUserServiceTest {

	@Mock
	private IOrganizerUserRepository organizerUserRepository;

	@InjectMocks
	private OrganizerUserService organizerUserService;
	
	// Factories
	private final OrganizerUserFactory organizerUserFactory = new OrganizerUserFactory();
	
	@Test
	void testFindByAppUserId() {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser();

		when(organizerUserRepository.findById(1L)).thenReturn(Optional.of(organizerUser));

		Optional<OrganizerUser> result = organizerUserService.findByAppUserId(1L);

		assertThat(result).isNotNull();
		assertThat(result.isPresent()).isTrue();
	}
	
	@Test
	void testFindByAppUserId_idIsNull() {
		Optional<OrganizerUser> result = organizerUserService.findByAppUserId(null);

		assertThat(result).isNotNull();
		assertThat(result.isEmpty()).isTrue();
	}

	@Test
	void testUpdate() {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser();

		when(organizerUserRepository.save(organizerUser)).thenReturn(organizerUser);

		OrganizerUser result = organizerUserService.update(organizerUser);

		assertThat(result).isNotNull();
	}
	
	@Test
	void testUpdate_null() {
		assertThatIllegalArgumentException().isThrownBy(() -> organizerUserService.update(null)).withMessage(EErrorMessages.NULL_VALUE.getMessage());
	}

	@Test
	void testDelete() {
		OrganizerUser organizerUser = organizerUserFactory.createOneOrganizerUser();

		organizerUserService.delete(organizerUser);

		assertThat(organizerUser).isNotNull();
	}
	
	@Test
	void testDelete_null() {
		assertThatIllegalArgumentException().isThrownBy(() -> organizerUserService.delete(null)).withMessage(EErrorMessages.NULL_VALUE.getMessage());
	}

}
