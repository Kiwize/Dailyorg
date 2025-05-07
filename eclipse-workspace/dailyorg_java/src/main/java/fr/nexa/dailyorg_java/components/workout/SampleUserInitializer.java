package fr.nexa.dailyorg_java.components.workout;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import fr.nexa.dailyorg_java.model.AppUser;
import fr.nexa.dailyorg_java.repository.workout.IAppUserRepository;

@Component
public class SampleUserInitializer implements CommandLineRunner {
    private final IAppUserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    
    public SampleUserInitializer(IAppUserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        String sampleEmail = "test@test.fr";
        
        // Check if user already exists
        if (userRepository.findByEmail(sampleEmail).isEmpty()) {
            AppUser testUser = new AppUser();
            testUser.setUsername("Nico");
            testUser.setPassword(passwordEncoder.encode("password123")); // Encrypt password
            testUser.setEmail(sampleEmail);
            testUser.setSurname("Nico");
            testUser.setRole("ROLE_USER");

            userRepository.save(testUser);
            System.out.println("Sample user created: " + sampleEmail);
        } else {
            System.out.println("Sample user already exists.");
        }
    }
}

