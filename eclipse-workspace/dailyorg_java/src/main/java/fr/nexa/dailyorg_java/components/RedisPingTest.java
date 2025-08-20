package fr.nexa.dailyorg_java.components;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;

@Component
public class RedisPingTest {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @PostConstruct
    public void ping() {
        try {
            redisTemplate.opsForValue().set("pingkey", "pong");
            System.out.println("Redis test OK");
        } catch (Exception e) {
            System.err.println("Redis test KO");
            Logger.getLogger(RedisPingTest.class.getName()).severe("Redis connection failed: " + e.getMessage());
        }
    }
}