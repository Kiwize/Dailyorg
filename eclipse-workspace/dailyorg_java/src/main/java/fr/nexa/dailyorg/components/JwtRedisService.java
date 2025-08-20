package fr.nexa.dailyorg.components;

import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class JwtRedisService {

	private final StringRedisTemplate redisTemplate;

	// Stocke le token avec une durée d’expiration
	public void storeToken(String token, long duration, TimeUnit unit) {
		redisTemplate.opsForValue().set(token, "valid", duration, unit);
	}

	// Vérifie si le token existe en base
	public boolean isTokenValid(String token) {
		if(token == null || token.isEmpty()) {
			return false;
		}
		return redisTemplate.hasKey(token);
	}

	// Supprime le token (pour une déconnexion ou révocation)
	public void deleteToken(String token) {
		redisTemplate.delete(token);
	}
}