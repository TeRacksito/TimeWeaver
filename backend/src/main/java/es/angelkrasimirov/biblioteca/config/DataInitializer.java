package es.angelkrasimirov.biblioteca.config;

import es.angelkrasimirov.biblioteca.models.Role;
import es.angelkrasimirov.biblioteca.models.User;
import es.angelkrasimirov.biblioteca.repositories.RoleRepository;
import es.angelkrasimirov.biblioteca.repositories.UserRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
public class DataInitializer {

	@Bean
	ApplicationRunner initRoles(RoleRepository roleRepository, UserRepository userRepository, PasswordEncoder passwordEncoder) {
		return args -> {
			List<String> defaultRoles = List.of("ROLE_USER", "ROLE_ADMIN");
			defaultRoles.forEach(roleName -> {
				roleRepository.findByName(roleName).orElseGet(() -> {
					Role role = new Role();
					role.setName(roleName);
					return roleRepository.save(role);
				});
			});

			// Initialize admin user
			String adminUsername = "root";
			userRepository.findByUsername(adminUsername).orElseGet(() -> {
				Role adminRole = roleRepository.findByName("ROLE_ADMIN")
						.orElseThrow(() -> new RuntimeException("Admin role not found"));

				Role userRole = roleRepository.findByName("ROLE_USER")
						.orElseThrow(() -> new RuntimeException("User role not found"));

				User adminUser = new User();
				adminUser.setUsername(adminUsername);
				adminUser.setPassword(passwordEncoder.encode("temporary_password_to_change"));
				adminUser.addRole(adminRole);
				adminUser.addRole(userRole);

				return userRepository.save(adminUser);
			});
		};
	}
}

