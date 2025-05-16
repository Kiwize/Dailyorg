package fr.nexa.dailyorg_java.service.dailyorg.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.model.dailyorg.OrganizerUser;
import fr.nexa.dailyorg_java.repository.dailyorg.IOrganizerUserRepository;

@ExtendWith(MockitoExtension.class)
public class OrganizerUserServiceTest {

	@Mock
	private IOrganizerUserRepository organizerUserRepository;

	@InjectMocks
	private OrganizerUserService organizerUserService;

	@Test
	void testFindByAppUserId() {

		// Mocking the AppUser object
		AppUser appUser = AppUser.builder().userId(1L).username("testUser").password("testPassword")
				.email("test@test.fr").build();

		OrganizerUser organizerUser = OrganizerUser.builder().organizerUserId(1L).appUser(appUser) // Mocked AppUser
				.build();

		when(organizerUserRepository.findByAppUser(appUser)).thenReturn(organizerUser);

		OrganizerUser result = organizerUserService.findByAppUserId(appUser);

		assertThat(result).isNotNull();
	}

	@Test
	void testCreate() {
		OrganizerUser organizerUser = OrganizerUser.builder().organizerUserId(1L)
				.appUser(AppUser.builder().userId(1L).build()).build();

		when(organizerUserRepository.save(organizerUser)).thenReturn(organizerUser);

		OrganizerUser result = organizerUserService.create(organizerUser);

		assertThat(result).isNotNull();
	}

	@Test
	void testUpdate() {
		OrganizerUser organizerUser = OrganizerUser.builder().organizerUserId(1L)
				.appUser(AppUser.builder().userId(1L).build()).build();

		when(organizerUserRepository.save(organizerUser)).thenReturn(organizerUser);

		OrganizerUser result = organizerUserService.update(organizerUser);

		assertThat(result).isNotNull();
	}

	@Test
	void testDelete() {
		OrganizerUser organizerUser = OrganizerUser.builder().organizerUserId(1L)
				.appUser(AppUser.builder().userId(1L).build()).build();

		organizerUserService.delete(organizerUser);

		assertThat(organizerUser).isNotNull();
	}

}
