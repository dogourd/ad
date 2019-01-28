package top.ezttf.ad.index.creative;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;

/**
 * 创意索引实现
 *
 * @author yuwen
 * @date 2019/1/27
 */
@Slf4j
@Component
public class CreativeIndex implements IIndexAware<Long, CreativeObject> {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 广告id --> 创意
     * redisKey : key: prefix + key; value: creative
     */
    private static final String AD_CREATIVE_INDEX_PREFIX = "ad_creative_index_prefix_";

    @Autowired
    public CreativeIndex(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CreativeObject get(Long key) {
        return (CreativeObject) redisTemplate.opsForValue().get(AD_CREATIVE_INDEX_PREFIX + key);
    }

    @Override
    public void add(Long key, CreativeObject value) {
        log.info("CreativeIndex, before add the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
        redisTemplate.opsForValue().set(AD_CREATIVE_INDEX_PREFIX + key, value);
        log.info("CreativeIndex, after add the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
    }

    @Override
    public void update(Long key, CreativeObject value) {
        log.info("CreativeIndex, before update the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
        CreativeObject creative = (CreativeObject) redisTemplate.opsForValue().get(AD_CREATIVE_INDEX_PREFIX + key);
        creative = creative == null ? value : creative.update(value);
        redisTemplate.opsForValue().set(AD_CREATIVE_INDEX_PREFIX + key, creative);
        log.info("CreativeIndex, after update the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
    }

    @Override
    public void delete(Long key, CreativeObject value) {
        log.info("CreativeIndex, before delete the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
        redisTemplate.delete(AD_CREATIVE_INDEX_PREFIX + key);
        log.info("CreativeIndex, after delete the key set is {}", redisTemplate.keys(AD_CREATIVE_INDEX_PREFIX + "*"));
    }
}
