package com.ansh.config;

import com.ansh.entity.subscription.Subscription;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.cache.RedisCacheWriter;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {

  @Bean
  public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
    RedisCacheWriter redisCacheWriter = RedisCacheWriter.nonLockingRedisCacheWriter(
        connectionFactory);
    GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();

    return new RedisCacheManager(redisCacheWriter, RedisCacheConfiguration.defaultCacheConfig()
        .serializeValuesWith(
            RedisSerializationContext.SerializationPair.fromSerializer(serializer)));
  }

  @Bean(name = "subscriptionRedisTemplate")
  @Primary
  public RedisTemplate<String, Subscription> subscriptionRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, Subscription> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
    return template;
  }

  @Bean(name = "updRedisTemplate")
  public RedisTemplate<String, String> updRedisTemplate(
      RedisConnectionFactory redisConnectionFactory) {
    RedisTemplate<String, String> template = new RedisTemplate<>();
    template.setConnectionFactory(redisConnectionFactory);
    template.setKeySerializer(new StringRedisSerializer());
    template.setValueSerializer(new StringRedisSerializer());
    return template;
  }
}
