package top.ezttf.ad.util;

import com.google.common.collect.Sets;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Collections;
import java.util.Set;

/**
 * @author yuwen
 * @date 2019/2/2
 */
public class RedisUtils {

    public static Set<String> scan(RedisTemplate<String, ?> redisTemplate, String pattern, int count) {
        ScanOptions scanOptions;
        if (count > -1) {
            scanOptions = ScanOptions.scanOptions().match(pattern).count(count).build();
        } else {
            scanOptions = ScanOptions.scanOptions().match(pattern).build();
        }
        ConvertingCursor<byte[], String> cursor = redisTemplate.executeWithStickyConnection((redisConnection) ->
                new ConvertingCursor<>(redisConnection.scan(scanOptions),
                        new StringRedisSerializer()::deserialize)
        );
        if (cursor != null) {
            Set<String> set = Sets.newHashSet();
            cursor.forEachRemaining(set::add);
            return set;
        }
        return Collections.emptySet();
    }

}
