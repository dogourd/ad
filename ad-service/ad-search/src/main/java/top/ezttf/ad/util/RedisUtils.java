package top.ezttf.ad.util;

import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;

/**
 * @author yuwen
 * @date 2019/2/2
 */
public class RedisUtils {

    public static Cursor<?> scan(RedisTemplate<String, ?> redisTemplate, String pattern, int count) {
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }

        return  redisTemplate.executeWithStickyConnection((redisConnection) ->
                new ConvertingCursor<>(redisConnection.scan(scanOptions),
                redisTemplate.getKeySerializer()::deserialize)
        );
    }

}
