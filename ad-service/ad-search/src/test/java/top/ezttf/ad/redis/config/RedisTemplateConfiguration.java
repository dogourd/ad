package top.ezttf.ad.redis.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * @author yuwen
 * @date 2019/1/25
 */
@Configuration
public class RedisTemplateConfiguration {

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        return new RedisTemplate<>();
    }
}
