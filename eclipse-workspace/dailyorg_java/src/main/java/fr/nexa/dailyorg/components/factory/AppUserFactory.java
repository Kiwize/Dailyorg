package fr.nexa.dailyorg.components.factory;

import com.github.javafaker.Faker;

import fr.nexa.dailyorg.model.AppUser;

public class AppUserFactory {
	
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
}
