package com.bank.config;

import com.bank.entity.User;
import com.bank.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DataInitializer {

    /** Creates a default admin (admin / admin123) on first run. */
    @Bean
    CommandLineRunner createAdmin(UserRepository repo, PasswordEncoder encoder) {
        return args -> {
            if (!repo.existsByUsername("admin")) {
                User admin = new User();
                admin.setUsername("admin");
                admin.setPassword(encoder.encode("admin123"));
                admin.setFullName("Bank Administrator");
                admin.setEmail("admin@sbbank.com");
                admin.setRole("ROLE_ADMIN");
                repo.save(admin);
            }
        };
    }
}
