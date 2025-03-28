package es.angelkrasimirov.timeweaver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.angelkrasimirov.timeweaver.dtos.AuthResponseDto;
import es.angelkrasimirov.timeweaver.dtos.UserLoginDto;
import es.angelkrasimirov.timeweaver.models.Role;
import es.angelkrasimirov.timeweaver.models.User;
import es.angelkrasimirov.timeweaver.services.AuthenticationService;
import es.angelkrasimirov.timeweaver.services.RoleService;
import es.angelkrasimirov.timeweaver.services.UserService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthenticationService authenticationService;

	@Autowired
	private UserController userController;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody UserLoginDto loginDto) throws InterruptedException {

		Thread.sleep(500 + (long) (Math.random() * 500));

		String token = authenticationService.login(loginDto);

		AuthResponseDto authResponseDto = new AuthResponseDto();
		authResponseDto.setAccessToken(token);

		return ResponseEntity.ok(authResponseDto);
	}

	// @PostMapping("/register")
	// public ResponseEntity<AuthResponseDto> createUser(@Valid @RequestBody User user) throws InterruptedException {
	// 	String password = user.getPassword();
	// 	User newUser = userController.createUser(user).getBody();

	// 	LoginDto loginDto = new LoginDto(newUser.getUsername(), password);
	// 	return login(loginDto);
	// }
}
