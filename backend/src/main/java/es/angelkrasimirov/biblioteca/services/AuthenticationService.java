package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.dtos.LoginDto;
import es.angelkrasimirov.biblioteca.utils.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	public String login(LoginDto loginDto) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
				loginDto.getUsername(),
				loginDto.getPassword()
		));

		SecurityContextHolder.getContext().setAuthentication(authentication);

		return jwtTokenProvider.generateToken(authentication);
	}
}
