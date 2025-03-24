package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.config.CustomUserDetails;
import es.angelkrasimirov.biblioteca.models.User;
import es.angelkrasimirov.biblioteca.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;

	public List<User> getAllUsers() {
		return userRepository.findAll();
	}

	public List<User> getUsersOrderedByLoans() {
		return userRepository.findAllUsersOrderedByLoans();
	}

	public User getUserById(Long id) {
		return userRepository.findById(id).orElse(null);
	}

	public User saveUser(User user) {
		return userRepository.save(user);
	}

	public User hashAndSaveUser(User user) {
		String hashedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(hashedPassword);
		return saveUser(user);
	}

	public void deleteUser(Long id) throws NoResourceFoundException {
		if (!userRepository.existsById(id)) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No user found with id " + id);
		}
		userRepository.deleteById(id);
	}

	public void deleteUser(User user) {
		userRepository.delete(user);
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"));

		Set<GrantedAuthority> authorities = user.getRoles().stream()
				.map((role) -> new SimpleGrantedAuthority(role.getName()))
				.collect(Collectors.toSet());

		return new CustomUserDetails(
				user.getId(),
				username,
				user.getPassword(),
				authorities
		);
	}
}
