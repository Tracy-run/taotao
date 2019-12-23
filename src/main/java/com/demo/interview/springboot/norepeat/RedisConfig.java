package com.demo.interview.springboot.norepeat;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

public class RedisConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.redis")
    public JedisConnectionFactory getConnectionFactory() {
        return new JedisConnectionFactory(new RedisStandaloneConfiguration(), JedisClientConfiguration.builder().build());
    }

    @Bean
    <K, V> RedisTemplate<K, V> getRedisTemplate() {
        RedisTemplate<K, V> redisTemplate = new RedisTemplate<K, V>();
        redisTemplate.setConnectionFactory(getConnectionFactory());
        return redisTemplate;
    }
}
