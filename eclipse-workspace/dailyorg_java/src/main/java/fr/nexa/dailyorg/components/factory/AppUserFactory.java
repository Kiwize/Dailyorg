package fr.nexa.dailyorg.components.factory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.github.javafaker.Faker;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.repository.IAppUserRepository;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__({@Autowired}))
public class AppUserFactory {
	
	private final IAppUserRepository appUserRepository;
	
	private final Faker faker = new Faker();
	
	public AppUser createOneAppUser() {
		return AppUser.builder()
				.email(faker.internet().emailAddress())
				.password(faker.internet().password(8, 16))
				.surname(faker.name().lastName())
				.username(faker.name().username())
				.role("ROLE_USER")
				.build();
	}
	
	public AppUser createAndInsertOneAppUser() {
		AppUser appUser = createOneAppUser();
		return appUserRepository.save(appUser);
	}

}
