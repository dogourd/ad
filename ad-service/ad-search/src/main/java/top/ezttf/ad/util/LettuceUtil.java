package top.ezttf.ad.util;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;

/**
 * 顺便熟悉一下lettuce api  不使用 {@link org.springframework.data.redis.core.RedisTemplate}
 *
 * @author yuwen
 * @date 2019/1/25
 */
@Configuration
public class LettuceUtil {

    @Bean
    public RedisTemplate<String, String> redisTemplate() {
        return new RedisTemplate<>();
    }

    //static {
    //    RedisClient redisClient = RedisClient.create("redis://127.0.0.1:6379/0");
    //    StatefulRedisConnection<String, String> connection = redisClient.connect();
    //    RedisCommands<String, String> commands = connection.sync();
    //}

    private RedisTemplate redisTemplate;



}
