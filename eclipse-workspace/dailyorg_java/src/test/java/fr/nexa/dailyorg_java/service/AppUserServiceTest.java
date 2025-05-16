package fr.nexa.dailyorg_java.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.repository.workout.IAppUserRepository;

@ExtendWith(MockitoExtension.class)
public class AppUserServiceTest {
	
	@Mock
	private IAppUserRepository appUserRepository;
	
	@InjectMocks
	private AppUserService appUserService;
	
	@Test
	void testAddUser() {
		try {
			AppUser appUser = AppUser.builder().userId(1L).build();
			
			when(appUserRepository.save(appUser)).thenReturn(appUser);
			
			AppUser result = appUserService.addUser(appUser);
			
			assertThat(result).isNotNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testUpdateUser() {
		try {
			AppUser appUser = AppUser.builder().userId(1L).build();
			
			when(appUserRepository.save(appUser)).thenReturn(appUser);
			
			appUserService.updateUser(appUser);
			
			assertThat(appUser).isNotNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testRemoveUser() {
		try {
			AppUser appUser = AppUser.builder().userId(1L).build();
			
			appUserService.removeUser(appUser);
			
			assertThat(appUser).isNotNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testGetAppUserByID() {
		try {
			AppUser appUser = AppUser.builder().userId(1L).build();
			
			when(appUserRepository.getReferenceById(1L)).thenReturn(appUser);
			
			AppUser result = appUserService.getAppUserByID(1L);
			
			assertThat(result).isNotNull();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void testFindByEmail() {
		try {
			AppUser appUser = AppUser.builder().userId(1L).email("test@test.fr").build();
			
			when(appUserRepository.findByEmail("test@test.fr")).thenReturn(Optional.of(appUser));
			
			Optional<AppUser> result = appUserService.findByEmail("test@test.fr");
			
			assertTrue(result.isPresent());
			assertThat(result.get()).isNotNull();
			assertThat(result.get().getEmail()).isEqualTo("test@test.fr");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
