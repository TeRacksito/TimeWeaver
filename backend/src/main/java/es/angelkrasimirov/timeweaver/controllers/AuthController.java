package es.angelkrasimirov.timeweaver.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import es.angelkrasimirov.timeweaver.dtos.AuthResponseDto;
import es.angelkrasimirov.timeweaver.dtos.LoginDto;
import es.angelkrasimirov.timeweaver.services.AuthenticationService;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

	@Autowired
	private AuthenticationService authenticationService;

	@PostMapping("/login")
	public ResponseEntity<AuthResponseDto> login(@RequestBody LoginDto loginDto) {
		String token = authenticationService.login(loginDto);

		AuthResponseDto authResponseDto = new AuthResponseDto();
		authResponseDto.setAccessToken(token);

		return ResponseEntity.ok(authResponseDto);
	}
}
