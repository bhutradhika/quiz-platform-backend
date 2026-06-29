package com.quiz.platform.shared.cache;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

/**
 * Service for Redis caching operations.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class CacheService {

    private final RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    /**
     * Stores a value in cache with TTL.
     *
     * @param key the cache key
     * @param value the value to cache
     * @param ttlSeconds time to live in seconds
     */
    public <T> void set(String key, T value, long ttlSeconds) {
        try {
            String json = objectMapper.writeValueAsString(value);
            redisTemplate.opsForValue().set(key, json, ttlSeconds, TimeUnit.SECONDS);
        } catch (JsonProcessingException e) {
            log.error("Failed to serialize value for key: {}", key, e);
        } catch (Exception e) {
            log.warn("Redis operation failed for key: {} - {}", key, e.getMessage());
        }
    }

    /**
     * Retrieves a value from cache with type conversion.
     *
     * @param key the cache key
     * @param type the expected type
     * @return the cached value or null
     */
    public <T> T get(String key, Class<T> type) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                return null;
            }
            return objectMapper.readValue(json, type);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize value for key: {}", key, e);
            return null;
        } catch (Exception e) {
            log.warn("Redis operation failed for key: {} - {}", key, e.getMessage());
            return null;
        }
    }

    /**
     * Retrieves a value from cache with TypeReference for generics.
     *
     * @param key the cache key
     * @param typeReference the type reference
     * @return the cached value or null
     */
    public <T> T get(String key, TypeReference<T> typeReference) {
        try {
            String json = redisTemplate.opsForValue().get(key);
            if (json == null) {
                return null;
            }
            return objectMapper.readValue(json, typeReference);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize value for key: {}", key, e);
            return null;
        } catch (Exception e) {
            log.warn("Redis operation failed for key: {} - {}", key, e.getMessage());
            return null;
        }
    }

    /**
     * Deletes a key from cache.
     *
     * @param key the cache key
     */
    public void delete(String key) {
        try {
            redisTemplate.delete(key);
        } catch (Exception e) {
            log.warn("Redis delete failed for key: {} - {}", key, e.getMessage());
        }
    }

    /**
     * Deletes all keys matching a pattern.
     *
     * @param pattern the key pattern
     */
    public void deleteByPattern(String pattern) {
        try {
            var keys = redisTemplate.keys(pattern);
            if (keys != null) {
                keys.forEach(redisTemplate::delete);
            }
        } catch (Exception e) {
            log.warn("Redis deleteByPattern failed for pattern: {} - {}", pattern, e.getMessage());
        }
    }

    /**
     * Checks if a key exists in cache.
     *
     * @param key the cache key
     * @return true if exists
     */
    public boolean exists(String key) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(key));
        } catch (Exception e) {
            log.warn("Redis exists failed for key: {} - {}", key, e.getMessage());
            return false;
        }
    }

    /**
     * Sets expiry on a key.
     *
     * @param key the cache key
     * @param ttlSeconds time to live in seconds
     */
    public void expire(String key, long ttlSeconds) {
        try {
            redisTemplate.expire(key, ttlSeconds, TimeUnit.SECONDS);
        } catch (Exception e) {
            log.warn("Redis expire failed for key: {} - {}", key, e.getMessage());
        }
    }
}
