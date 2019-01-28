package top.ezttf.ad.index.adplan;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;

/**
 * 推广计划索引实现
 *
 * @author yuwen
 * @date 2019/1/25
 */
@Slf4j
@Component
public class AdPlanIndex implements IIndexAware<Long, AdPlanObject> {

    /**
     * 推广计划索引 redis_key 前缀。
     */
    private static final String AD_PLAN_INDEX_PREFIX = "ad_plan_index_prefix_";

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public AdPlanIndex(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }



    @Override
    public AdPlanObject get(Long key) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
        return (AdPlanObject) redisTemplate.opsForValue().get(redisKey);
    }

    @Override
    public void add(Long key, AdPlanObject value) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
        log.info("AdPlanIndex, before add the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
        redisTemplate.opsForValue().setIfAbsent(redisKey, value);
        log.info("AdPlanIndex, after add the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
    }

    @Override
    public void update(Long key, AdPlanObject value) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
        log.info("AdPlanIndex, before update the key is {}, the value is {}", redisKey, redisTemplate.opsForValue().get(redisKey));
        AdPlanObject oldObject = (AdPlanObject) redisTemplate.opsForValue().get(key.toString());
        value = oldObject == null ? value : oldObject.update(value);
        redisTemplate.opsForValue().set(key.toString(), value);
        log.info("AdPlanIndex, after update the key is {}, the value is {}", redisKey, redisTemplate.opsForValue().get(redisKey));
    }

    @Override
    public void delete(Long key, AdPlanObject value) {
        String redisKey = AD_PLAN_INDEX_PREFIX + key;
        log.info("AdPlanIndex, before delete the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
        redisTemplate.delete(redisKey);
        log.info("AdPlanIndex, after delete the key set is {}", redisTemplate.keys(AD_PLAN_INDEX_PREFIX + "*"));
    }
}
