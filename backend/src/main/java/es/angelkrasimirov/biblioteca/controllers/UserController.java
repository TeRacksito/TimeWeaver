package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.Role;
import es.angelkrasimirov.biblioteca.models.User;
import es.angelkrasimirov.biblioteca.services.RoleService;
import es.angelkrasimirov.biblioteca.services.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

//	@PreAuthorize("hasRole('ADMIN')")
//	@GetMapping("/users")
//	public ResponseEntity<List<User>> getUsers() {
//		return ResponseEntity.ok(userService.getAllUsers());
//	}

	@PreAuthorize("hasRole('ADMIN')")
	@GetMapping("/users")
	public ResponseEntity<List<User>> getUsers(
			@RequestParam(name = "orderByLoanCount", required = false, defaultValue = "false") Boolean orderByLoanCount
	) {
		List<User> users;

		if (orderByLoanCount) {
			users = userService.getUsersOrderedByLoans();
		} else {
			users = userService.getAllUsers();
		}

		return ResponseEntity.ok(users);
	}

	@GetMapping("/users/{userId}")
	public ResponseEntity<User> getUserById(@PathVariable Long userId) {
		User user = userService.getUserById(userId);

		if (user == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(user);
	}

	@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
	@PutMapping("/users/{userId}")
	public ResponseEntity<User> updateUser(@PathVariable Long userId, @Valid @RequestBody User user) {
		User existingUser = userService.getUserById(userId);

		if (existingUser == null) {
			return ResponseEntity.notFound().build();
		}

		existingUser.setUsername(user.getUsername());
		existingUser.setPassword(user.getPassword());

		User updatedUser = userService.hashAndSaveUser(existingUser);

		return ResponseEntity.ok(updatedUser);
	}

	@PostMapping("/users")
	public ResponseEntity<User> createUser(@Valid @RequestBody User user) {
		Role userRole = roleService.findRoleByName("ROLE_USER");
		user.addRole(userRole);
		User newUser = userService.hashAndSaveUser(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
	}


	@PreAuthorize("hasRole('ADMIN') or #userId == authentication.principal.id")
	@DeleteMapping("/users/{userId}")
	public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
		try {
			userService.deleteUser(userId);
			return ResponseEntity.ok().build();
		} catch (NoResourceFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}


//	@PostMapping("/users/register")
//	public ResponseEntity<User> registerUser(@RequestBody User user) {
//		User newUser = userService.registerUser(user);
//		return ResponseEntity.ok(newUser);
//	}
//
//	@PostMapping("/users/login")
//	public ResponseEntity<String> loginUser(@RequestBody User user) {
//		String token = authenticationService.authenticateUser(user);
//		return ResponseEntity.ok(token);
//	}
}
