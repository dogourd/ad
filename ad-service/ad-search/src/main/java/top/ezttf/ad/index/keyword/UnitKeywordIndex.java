package top.ezttf.ad.index.keyword;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * 关键词索引对象的实现
 * 考虑到这种推广单元的限制维度, 是以关键词的形式去寻找推广单元的id, 应该使用倒排索引
 * 一个关键词可以对应到多个推广单元id上面
 *
 * @author yuwen
 * @date 2019/1/26
 */
@Slf4j
@Component
public class UnitKeywordIndex implements IIndexAware<String, Set<Long>> {

    /**
     * keyword --> unit 索引  redis_key 前缀       倒排索引
     * <p>
     * keyword ---> UnitIdSet
     */
    private static final String KEYWORD_UNIT_INDEX_PREFIX = "keyword_unit_index_prefix_";

    /**
     * unit --> keyword 索引  redis_key 前缀       正向索引
     * <p>
     * unitId ---> keywordSet
     */
    private static final String UNIT_KEYWORD_INDEX_PREFIX = "unit_keyword_index_prefix_";

    private final RedisTemplate<String, Object> redisTemplate;

    @Autowired
    public UnitKeywordIndex(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }


    /**
     * 通过关键词(keyword 推广单元支持的关键词)
     * 获取推广单元的id set
     *
     * @param key keyword
     * @return
     */
    @Override
    @SuppressWarnings("unchecked")
    public Set<Long> get(String key) {
        Set<Long> unitIdSetResult = Sets.newHashSet();
        if (StringUtils.isBlank(key)) {
            return Collections.emptySet();
        }
        String redisKey = KEYWORD_UNIT_INDEX_PREFIX + key;
        Set unitIdSet = redisTemplate.opsForSet().members(redisKey);
        if (unitIdSet == null) {
            return Collections.emptySet();
        }
        unitIdSet.forEach(unitId -> unitIdSetResult.add(Long.valueOf(unitId.toString())));
        return unitIdSetResult;
    }

    /**
     * 新增关键词
     *
     * @param key
     * @param value
     */
    @Override
    public void add(String key, Set<Long> value) {
        // key: keyword; value: unitIdSet
        String keywordUnitRedisKey = KEYWORD_UNIT_INDEX_PREFIX + key;
        log.info("UnitKeywordIndex, before add the key set is {}", redisTemplate.keys(UNIT_KEYWORD_INDEX_PREFIX + "*"));
        // key: prefix + keyword; value: Set<Long> unitIdSet
        redisTemplate.opsForSet().add(keywordUnitRedisKey, value.toArray());
        for (Long unitId : value) {
            // key: prefix + unitId; value: keyword
            redisTemplate.opsForSet().add(UNIT_KEYWORD_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitKeywordIndex, after add the key set is {}", redisTemplate.keys(UNIT_KEYWORD_INDEX_PREFIX + "*"));
    }

    /**
     * 因为两个索引对应相互关联  每一个索引的任何一个key 都会对应到一个 set
     * 所以更新的成本会 很高, 因为需要对 set进行遍历,
     * 所以此处 不支持更新
     *
     * @param key
     * @param value
     */
    @Override
    public void update(String key, Set<Long> value) {
        log.error("keyword index update is not supported ");
    }

    /**
     * 因为此处的key keyword可能是一部分unitIdList, 并不是所有的
     * 所以不能直接就将其删除掉, 可能只是删除掉一部分 keyword 到 unitIds的映射
     *
     * @param key
     * @param value
     */
    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitKeywordIndex, before delete the key set is {}", redisTemplate.keys(KEYWORD_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().remove(KEYWORD_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().remove(UNIT_KEYWORD_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitKeywordIndex, after delete the key set is {}", redisTemplate.keys(KEYWORD_UNIT_INDEX_PREFIX + "*"));
    }


    /**
     * 开放方法用来匹配 某个推广单元unitId 是否包含了一些关键词 List<String> keyWords
     */
    @SuppressWarnings({"all"})
    public boolean match(Long unitId, List<String> keywords) {
        if (redisTemplate.hasKey(UNIT_KEYWORD_INDEX_PREFIX + unitId)) {
            Set<Object> unitKeywords = redisTemplate.opsForSet().members(UNIT_KEYWORD_INDEX_PREFIX + unitId);
            if (CollectionUtils.isNotEmpty(unitKeywords)) {
                // 判断 keywords 是不是 unitKeywords的子集
                return CollectionUtils.isSubCollection(keywords, unitKeywords);
            }
        }
        return false;
    }
}