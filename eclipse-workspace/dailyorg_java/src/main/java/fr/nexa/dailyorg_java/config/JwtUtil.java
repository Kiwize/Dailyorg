package fr.nexa.dailyorg_java.config;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
	
	@Value("${JWT_SECRET}")
    private String SECRET_KEY;

    public String generateToken(String username) {
    	System.out.println("Username : " + SECRET_KEY);
    	
        return Jwts.builder()
            .setSubject(username)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
            .signWith(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
            .compact();
    }

    public String extractUsername(String token) {
    	return Jwts.parserBuilder()
    	        .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes()))
    	        .build()
    	        .parseClaimsJws(token)
    	        .getBody()
    	        .getSubject();
    }

    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return Jwts.parserBuilder()
            .setSigningKey(Keys.hmacShaKeyFor(SECRET_KEY.getBytes())) // Use the same key as in signing
            .build()
            .parseClaimsJws(token)
            .getBody()
            .getExpiration()
            .before(new Date());
    }
}

