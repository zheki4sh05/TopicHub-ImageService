package web.forum.imageservice.config;

import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import org.springframework.data.redis.cache.*;
import org.springframework.data.redis.connection.*;
import org.springframework.data.redis.connection.jedis.*;
import org.springframework.data.redis.core.*;
import org.springframework.data.redis.serializer.*;

import java.time.*;

@Configuration
public class ApplicationConfig {

    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private Integer port;


    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.json()));
    }

    @Bean
    public RedisConnectionFactory jedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        return new JedisConnectionFactory(redisStandaloneConfiguration);
    }
    @Bean
    public RedisTemplate<Long, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<Long, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        return template;
    }
}
