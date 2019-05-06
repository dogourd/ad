package top.ezttf.ad.index.adunit;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;
import top.ezttf.ad.util.RedisUtils;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 推广单元索引实现
 *
 * @author yuwen
 * @date 2019/1/26
 */
@Slf4j
@Component
public class AdUnitIndex implements IIndexAware<Long, AdUnitObject> {

    /**
     * 推广单元索引 redis_key 前缀
     */
    private static final String AD_UNIT_INDEX_PREFIX = "ad_unit_index_prefix_";

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public AdUnitIndex(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    public AdUnitObject get(Long key) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        return (AdUnitObject) redisTemplate.opsForValue().get(redisKey);
    }

    /**
     * 匹配某一流量类型的 广告单元, 返回单元id 集合
     * @param positionType
     * @return
     */
    public Set<Long> match(int positionType) {
        Set<Long> adUnitIds = Sets.newHashSet();
        Set<String> matchKeySet = RedisUtils.scan(redisTemplate, AD_UNIT_INDEX_PREFIX + "*", -1);
        matchKeySet.forEach(redisKey -> {
            if (AdUnitObject.isAdSlotTypeOk(positionType,
                    ((AdUnitObject) redisTemplate.opsForValue().get(redisKey)).getPositionType())) {
                adUnitIds.add(Long.valueOf(StringUtils.remove(redisKey, AD_UNIT_INDEX_PREFIX)));
            }
        });
        return adUnitIds;
    }

    public List<AdUnitObject> fetch(Collection<Long> adUnitIds) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return Collections.emptyList();
        }
        List<AdUnitObject> unitObjects = Lists.newArrayList();
        adUnitIds.forEach(unitId -> {
            AdUnitObject unitObject = get(unitId);
            if (unitObject == null) {
                log.error("adUnitObject not found: {}", unitId);
                return;
            }
            unitObjects.add(unitObject);
        });
        return unitObjects;
    }

    @Override
    public void add(Long key, AdUnitObject value) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        log.info("AdUnitIndex, before add the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForValue().set(redisKey, value);
        log.info("AdUnitIndex, after add the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
    }

    @Override
    public void update(Long key, AdUnitObject value) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        log.info("AdUnitIndex, before update the key {}, value is {}", key, redisTemplate.opsForValue().get(redisKey));
        AdUnitObject oldObject = (AdUnitObject) redisTemplate.opsForValue().get(redisKey);
        value = oldObject == null ? value : oldObject.update(value);
        redisTemplate.opsForValue().set(redisKey, value);
        log.info("AdUnitIndex, after update the key {}, value is {}", key, redisTemplate.opsForValue().get(redisKey));
    }

    @Override
    public void delete(Long key, AdUnitObject value) {
        String redisKey = AD_UNIT_INDEX_PREFIX + key;
        log.info("AdUnitIndex, before delete the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.delete(redisKey);
        log.info("AdUnitIndex, after delete the key set is {}", redisTemplate.keys(AD_UNIT_INDEX_PREFIX + "*"));
    }
}
