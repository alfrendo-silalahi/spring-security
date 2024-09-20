package dev.alfrendosilalahi.spring.security.service.impl;

import dev.alfrendosilalahi.spring.security.exception.ResourceNotFoundException;
import dev.alfrendosilalahi.spring.security.service.RedisService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RedisServiceImpl implements RedisService {

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public <T> void putValue(String key, T value) {
        redisTemplate.opsForValue().set(key, value);
    }

    @Override
    public <T> T findObject(String key, Class<T> returnType) {
        Object obj = redisTemplate.opsForValue().get(key);
        if (Objects.isNull(obj)) {
            throw new ResourceNotFoundException("cache", "key", key);
        }
        return returnType.cast(obj);
    }

}
