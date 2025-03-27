package es.angelkrasimirov.timeweaver.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtTokenProvider {

	@Value("${jwt.secret}")
	private String jwtSecret;

	public String generateToken(Authentication authentication) {

		String username = authentication.getName();
		Date now = new Date();
		long oneHourInMilliseconds = 60 * 60 * 1000;
		Date validity = new Date(now.getTime() + oneHourInMilliseconds);

		return Jwts.builder()
				.subject(username)
				.issuedAt(now)
				.expiration(validity)
				.signWith(getSigningKey())
				.compact();

	}

	public Claims parseClaims(String token) throws JwtException {
		return Jwts.parser()
				.verifyWith(getSigningKey())
				.build()
				.parseSignedClaims(token)
				.getPayload();
	}

	public String getUsername(String token) throws JwtException {
		return parseClaims(token).getSubject();
	}

	public boolean isTokenExpired(String token) throws JwtException {
		return parseClaims(token).getExpiration().before(new Date());
	}

	public boolean validateToken(String token) throws JwtException {
		return !isTokenExpired(token);
	}

	private SecretKey getSigningKey() {
		return Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtSecret));
	}


}
