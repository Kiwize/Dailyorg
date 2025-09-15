package fr.nexa.dailyorg.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import fr.nexa.dailyorg.components.JwtRedisService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.Cookie;

@Component
public class JwtUtil {

	@Value("${JWT_SECRET}")
	private String SECRET_KEY;

	private final JwtRedisService jwtRedisService;

	public JwtUtil(JwtRedisService jwtRedisService) {
		this.jwtRedisService = jwtRedisService;
	}

	/** Generate a JWT token for the given username.
	 * 
	 * @param username The username for which the token is to be generated.
	 * 
	 * @return A JWT token as a String.
	 */
	public String generateToken(String username) {
		return Jwts.builder()
				.setSubject(username)
				.setIssuedAt(new Date()) 
				.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
				.signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.compact();
	}

	/**
	 * Extract the username from the JWT token present in the cookies.
	 * 
	 * @param cookies An array of Cookie objects from which to extract the JWT.
	 * @return The username if the JWT is found and valid, otherwise null.
	 */
	public String extractUsernameFromCookies(Cookie[] cookies) {
		return extractUsername(extractJWTFromCookies(cookies));
	}

	/**
	 * Extract the JWT token from the cookies.
	 * 
	 * @param cookies An array of Cookie objects from which to extract the JWT.
	 * @return The JWT token if found, otherwise null.
	 */
	public String extractJWTFromCookies(Cookie[] cookies) {
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if ("jwt".equals(cookie.getName())) {
					return cookie.getValue();
				}
			}
		}
		return null; // No JWT cookie found
	}

	/**
	 * Extract the username from the given JWT token.
	 * 
	 * @param token The JWT token from which to extract the username.
	 * @return The username if the token is valid, otherwise null.
	 */
	public String extractUsername(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}

	/**
	 * Validate the JWT token against the provided username.
	 * 
	 * @param token    The JWT token to validate.
	 * @param username The username to compare against the token's subject.
	 * @return true if the token is valid and matches the username, otherwise false.
	 */
	public boolean isTokenValid(String token, String username) {
		return jwtRedisService.isTokenValid(token) && username.equals(extractUsername(token)) && !isTokenExpired(token);
	}

	/**
	 * Check if the JWT token has expired.
	 * 
	 * @param token The JWT token to check.
	 * @return true if the token has expired, otherwise false.
	 */
	private boolean isTokenExpired(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
				.build()
				.parseClaimsJws(token)
				.getBody().getExpiration().before(new Date());
	}
}
