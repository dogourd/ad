package top.ezttf.ad.index.creativeunit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;

/**
 * 创意与推广单元关联索引实现
 *
 * @author yuwen
 * @date 2019/1/27
 */
@Slf4j
@Component
public class CreativeUnitIndex implements IIndexAware<String, CreativeUnitObject> {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 创意id + 推广单元 id ---> creativeUnitObject
     * key: adId + "-" + unitId; value: creative
     * redisKey : key: prefix + adId + "-" + unitId; value: creative
     */
    private static final String AD_UNIT_CRETIVE_INDEX_PREFIX = "ad_unit_creative_index_prefix_";
    /**
     * 创意id --> 推广单元id Set
     * key: adId; value: unitIdSet
     * redisKey : key: prefix + adId; value: unitIdSet
     */
    private static final String AD_UNIT_INDEX_PREFIX = "ad_unit_index_prefix_";
    /**
     * 推广单元 id --> 创意id Set
     * key: unitId; value: adIdSet
     * redisKey : key: prefix + unitId; value: adIdSet
     */
    private static final String UNIT_AD_INDEX_PREFIX = "unit_ad_index_prefix_";
    @Autowired
    public CreativeUnitIndex(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public CreativeUnitObject get(String key) {
        return (CreativeUnitObject) redisTemplate.opsForValue().get(AD_UNIT_CRETIVE_INDEX_PREFIX + key);
    }

    @Override
    public void add(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex, before add the key set is {}", redisTemplate.keys(AD_UNIT_CRETIVE_INDEX_PREFIX + "*"));
        redisTemplate.opsForValue().set(AD_UNIT_CRETIVE_INDEX_PREFIX + key, value);
        redisTemplate.opsForSet().add(AD_UNIT_INDEX_PREFIX + value.getAdId(), value.getUnitId());
        redisTemplate.opsForSet().add(UNIT_AD_INDEX_PREFIX + value.getUnitId(), value.getAdId());
        log.info("CreativeUnitIndex, after add the key set is {}", redisTemplate.keys(AD_UNIT_CRETIVE_INDEX_PREFIX + "*"));

    }

    @Override
    public void update(String key, CreativeUnitObject value) {
        log.error("CreativeUnitIndex, creativeUnit index update is not supported");
    }

    @Override
    public void delete(String key, CreativeUnitObject value) {
        log.info("CreativeUnitIndex, before delete the key set is {}", redisTemplate.keys(AD_UNIT_CRETIVE_INDEX_PREFIX + "*"));
        redisTemplate.delete(AD_UNIT_CRETIVE_INDEX_PREFIX + key);
        redisTemplate.opsForSet().remove(AD_UNIT_INDEX_PREFIX + value.getAdId(), value.getUnitId());
        redisTemplate.opsForSet().remove(UNIT_AD_INDEX_PREFIX + value.getUnitId(), value.getAdId());
        log.info("CreativeUnitIndex, after delete the key set is {}", redisTemplate.keys(AD_UNIT_CRETIVE_INDEX_PREFIX + "*"));
    }
}
