package fr.nexa.dailyorg.components;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class RedisPingTest {

    private final StringRedisTemplate redisTemplate;

    @PostConstruct
    public void ping() {
        try {
            redisTemplate.opsForValue().set("pingkey", "pong");
        } catch (Exception e) {
            Logger.getLogger(RedisPingTest.class.getName()).severe("Redis connection failed: " + e.getMessage());
        }
    }
}