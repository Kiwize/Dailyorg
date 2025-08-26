package fr.nexa.dailyorg.components;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg.model.AppUser;
import fr.nexa.dailyorg.repository.IAppUserRepository;
import fr.nexa.dailyorg.utils.EErrorMessages;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CustomUserDetailsService implements UserDetailsService {

	private final IAppUserRepository userRepository;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<AppUser> user = userRepository.findByEmail(email);
		if (user.isEmpty()) {
			throw new UsernameNotFoundException(EErrorMessages.USER_NOT_FOUND.getMessage() + email);
		} else {
			AppUser appUser = user.get();
			
			return new User(appUser.getEmail(), appUser.getPassword(), // This must be a hashed password!
					List.of(new SimpleGrantedAuthority("ROLE_USER")));
		}
	}
}
