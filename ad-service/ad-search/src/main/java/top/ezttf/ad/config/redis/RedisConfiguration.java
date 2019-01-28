package top.ezttf.ad.config.redis;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

/**
 * @author yuwen
 * @date 2019/1/26
 */
@Configuration
public class RedisConfiguration {

    @Value("${spring.redis.host}")
    private String host;
    @Value("${spring.redis.port}")
    private Integer port;
    @Value("${spring.redis.timeout}")
    private Integer timeout;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private Integer maxIdle;
    @Value("${spring.redis.lettuce.pool.max-wait}")
    private Long maxWait;
    @Value("${spring.redis.lettuce.pool.max-active}")
    private Integer maxActive;
    @Value("${spring.redis.lettuce.pool.max-idle}")
    private Integer minIdle;
    @Value("${spring.redis.database}")
    private Integer database;

    @Bean
    public RedisStandaloneConfiguration standaloneConfiguration() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(database);
        return configuration;
    }

    @Bean
    public GenericObjectPoolConfig genericObjectPoolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxIdle(maxIdle);
        poolConfig.setMaxTotal(maxActive);
        poolConfig.setMinIdle(minIdle);
        poolConfig.setMaxWaitMillis(maxWait);
        poolConfig.setTestOnBorrow(true);
        poolConfig.setTestOnCreate(true);
        poolConfig.setTestWhileIdle(true);
        return poolConfig;
    }

    @Bean
    public LettuceConnectionFactory lettuceConnectionFactory() {
        LettucePoolingClientConfiguration configuration = LettucePoolingClientConfiguration.builder()
                .poolConfig(genericObjectPoolConfig())
                .commandTimeout(Duration.ofMillis(timeout))
                .build();
        LettuceConnectionFactory factory = new LettuceConnectionFactory(standaloneConfiguration(), configuration);
        factory.setShareNativeConnection(true);
        return factory;
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());

        Jackson2JsonRedisSerializer<Object> redisSerializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
        objectMapper.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
        objectMapper.registerModule(new JavaTimeModule())
                .registerModule(new Jdk8Module())
                .registerModule(new ParameterNamesModule());
        objectMapper.findAndRegisterModules();
        redisSerializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(redisSerializer);
        StringRedisSerializer stringRedisSerializer = new StringRedisSerializer(StandardCharsets.UTF_8);
        redisTemplate.setKeySerializer(stringRedisSerializer);

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }



    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        StringRedisTemplate redisTemplate = new StringRedisTemplate();
        redisTemplate.setConnectionFactory(lettuceConnectionFactory());
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
