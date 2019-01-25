package top.ezttf.ad.redis;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.context.junit4.SpringRunner;
import top.ezttf.ad.redis.config.RedisTemplateConfiguration;

/**
 * @author yuwen
 * @date 2019/1/25
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
@AutoConfigureAfter(value = RedisTemplateConfiguration.class)
public class TestRedisTemplate {

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @Test
    public void set() {
        redisTemplate.opsForValue().set("String_key", "String_value");
    }

    @Test
    public void get() {
        log.debug("{}", redisTemplate.opsForValue().get("String_key"));
    }


}
