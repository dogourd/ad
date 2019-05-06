package top.ezttf.ad.index.district;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.index.IIndexAware;
import top.ezttf.ad.search.vo.feature.DistrictFeature;
import top.ezttf.ad.util.CommonUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 地域索引
 *
 * @author yuwen
 * @date 2019/1/27
 */
@Slf4j
@Component
public class UnitDistrictIndex implements IIndexAware<String, Set<Long>> {

    private final RedisTemplate<String, Object> redisTemplate;

    /**
     * 地域(省市) --> 推广单元
     * key: district(province + "-" + city); value: unitIdSet
     * redisKey : key: prefix + province + "-" + city; value: unitIdSet
     */
    private static final String DISTRICT_UNIT_INDEX_PREFIX = "district_unit_index_prefix_";

    /**
     * 推广单元 --> 地域(省市)
     * key: unitId --> district(province + "-" + city)
     * reidsKey : key: unitId; value: districtSet(prefix + province + "-" + city)
     */
    private static final String UNIT_DISTRICT_INDEX_PREFIX = "unit_district_index_prefix_";

    @Autowired
    public UnitDistrictIndex(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    @SuppressWarnings("all")
    public Set<Long> get(String key) {
        return (Set) redisTemplate.opsForSet().members(DISTRICT_UNIT_INDEX_PREFIX + key);
    }

    @SuppressWarnings("all")
    public boolean match(Long adUnitId, List<DistrictFeature.ProvinceAndCity> districts) {
        if (redisTemplate.hasKey(UNIT_DISTRICT_INDEX_PREFIX + adUnitId) &&
                CollectionUtils.isNotEmpty(
                        redisTemplate.opsForSet()
                                .members(UNIT_DISTRICT_INDEX_PREFIX + adUnitId))) {
            Set unitDistricts = redisTemplate.opsForSet().members(UNIT_DISTRICT_INDEX_PREFIX + adUnitId);
            if (CollectionUtils.isNotEmpty(unitDistricts)) {
                List<String> targetDistrict = districts.stream()
                        .map(district -> CommonUtils.stringContact(district.getProvince(), district.getCity()))
                        .collect(Collectors.toList());
                return CollectionUtils.isSubCollection(targetDistrict, unitDistricts);
            }
        }
        return false;
    }


    @Override
    public void add(String key, Set<Long> value) {
        log.info("UnitDistrictIndex, before add the key set is {}",
                redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().add(DISTRICT_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().add(UNIT_DISTRICT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitDistrictIndex, after add the key set is {}",
                redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));

    }

    @Override
    public void update(String key, Set<Long> value) {
        log.error("UnitDistrictIndex, district index update is not supported");
    }

    @Override
    public void delete(String key, Set<Long> value) {
        log.info("UnitDistrictIndex, before delete the key set is {}",
                redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));
        redisTemplate.opsForSet().remove(DISTRICT_UNIT_INDEX_PREFIX + key, value.toArray());
        for (Long unitId : value) {
            redisTemplate.opsForSet().remove(UNIT_DISTRICT_INDEX_PREFIX + unitId, key);
        }
        log.info("UnitDistrictIndex, after delete the key set is {}",
                redisTemplate.keys(DISTRICT_UNIT_INDEX_PREFIX + "*"));
    }


}
