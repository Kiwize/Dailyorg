package fr.nexa.dailyorg_java.service.workout.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.nexa.dailyorg_java.model.workout.AppUser;
import fr.nexa.dailyorg_java.repository.workout.IAppUserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Autowired
    private IAppUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    	AppUser user = userRepository.findByEmail(email).get();

        return new org.springframework.security.core.userdetails.User(
            user.getEmail(),
            user.getPassword(), // This must be a hashed password!
            List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }
}

