package top.ezttf.ad.index.interest;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;

import java.util.List;
import java.util.Set;

/**
 * 兴趣索引
 * key: 兴趣标签 ; value: unitIdSet
 *
 * @author yuwen
 * @date 2019/1/27
 */
@Slf4j
@Component
public class UnitItIndex implements IIndexAware<String, Set<Long>> {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 倒排索引
     * key: itTag(String); value: adUnitIdSet(Set<Long>)
     * redis:  key : prefix + itTag; value: adUnitIdSet
     */
    private static final String IT_UNIT_INDEX_PREFIX = "it_unit_index_prefix_";

    /**
     * 正向索引
     * key: adUnitId(Long); value: itTagSet(Set<String>)
     * redis: key : prefix + unitId; value: itTagSet
     */
    private static final String UNIT_IT_INDEX_PREFIX = "unit_it_index_prefix_";

    @Autowired
    public UnitItIndex(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @SuppressWarnings("unchecked")
    public Set<Long> get(String key) {
        return (Set) redisTemplate.opsForSet().members(IT_UNIT_INDEX_PREFIX + key);
    }

    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitItIndex, before add the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().add(IT_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().add(UNIT_IT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitItIndex, after add the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("UnitItIndex, it index update is not supported");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitItIndex, before delete the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().remove(IT_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().remove(UNIT_IT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitItIndex, after delete the key set is {}", redisTemplate.keys(IT_UNIT_INDEX_PREFIX + "*"));
    }


    /**
     * 查询 unitId 对应的兴趣标签是否包含 itTagSet集合
     * @param unitId
     * @param itTagSet
     * @return
     */
    @SuppressWarnings("all")
    public boolean match(Long unitId, List<String> itTagSet) {
        if (redisTemplate.hasKey(UNIT_IT_INDEX_PREFIX + unitId)) {
            Set<Object> unitItTagSet = redisTemplate.opsForSet().members(UNIT_IT_INDEX_PREFIX + unitId);
            if (CollectionUtils.isNotEmpty(unitItTagSet)) {
                // itTagSet 是否为 unitTagSet的子集
                return CollectionUtils.isSubCollection(itTagSet, unitItTagSet);
            }
        }
        return false;
    }
}
