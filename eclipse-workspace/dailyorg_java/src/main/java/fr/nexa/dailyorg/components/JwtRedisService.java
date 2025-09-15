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

	/**
	 * Stores a token in Redis with a specified duration.
	 * @param token
	 * @param duration
	 * @param unit
	 */
	public void storeToken(String token, long duration, TimeUnit unit) {
		redisTemplate.opsForValue().set(token, "valid", duration, unit);
	}

	/**
	 * Checks if a token is valid (exists in Redis).
	 * @param token
	 * @return
	 */
	public boolean isTokenValid(String token) {
		if(token == null || token.isEmpty()) {
			return false;
		}
		return redisTemplate.hasKey(token);
	}

	/**
	 * Deletes a token from Redis.
	 * @param token
	 */
	public void deleteToken(String token) {
		redisTemplate.delete(token);
	}
}