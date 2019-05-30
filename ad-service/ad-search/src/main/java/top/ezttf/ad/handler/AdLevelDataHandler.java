package top.ezttf.ad.handler;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import top.ezttf.ad.constant.OpType;
import top.ezttf.ad.dump.table.*;
import top.ezttf.ad.index.DataTable;
import top.ezttf.ad.index.IIndexAware;
import top.ezttf.ad.index.adplan.AdPlanIndex;
import top.ezttf.ad.index.adplan.AdPlanObject;
import top.ezttf.ad.index.adunit.AdUnitIndex;
import top.ezttf.ad.index.adunit.AdUnitObject;
import top.ezttf.ad.index.creative.CreativeIndex;
import top.ezttf.ad.index.creative.CreativeObject;
import top.ezttf.ad.index.creativeunit.CreativeUnitIndex;
import top.ezttf.ad.index.creativeunit.CreativeUnitObject;
import top.ezttf.ad.index.district.UnitDistrictIndex;
import top.ezttf.ad.index.interest.UnitItIndex;
import top.ezttf.ad.index.keyword.UnitKeywordIndex;
import top.ezttf.ad.util.CommonUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 1. 因为索引之间存在着关联关系, 比如推广单元需要有推广计划的存在。 即索引之间存在着层级的划分
 * 用户层级 ---> 推广计划 --->推广单元 ---->推广单元限制...
 * 2. 全量索引其实是 增量索引 "添加" 的一种特殊实现
 *
 * @author yuwen
 * @date 2019/1/29
 */
@Slf4j
public class AdLevelDataHandler {

    public static void handleLevel2(AdPlanTable adPlanTable, OpType type) {
        AdPlanObject planObject = new AdPlanObject(
                adPlanTable.getId(),
                adPlanTable.getUserId(),
                adPlanTable.getPlanStatus(),
                adPlanTable.getStartDate(),
                adPlanTable.getEndDate()
        );
        handleBinlogEvent(
                DataTable.of(AdPlanIndex.class),
                planObject.getPlanId(),
                planObject,
                type
        );
    }

    public static void handleLevel2(AdCreativeTable creativeTable, OpType type) {
        CreativeObject creativeObject = new CreativeObject(
                creativeTable.getAdId(),
                creativeTable.getName(),
                creativeTable.getType(),
                creativeTable.getMaterialType(),
                creativeTable.getHeight(),
                creativeTable.getWidth(),
                creativeTable.getAuditStatus(),
                creativeTable.getAdUrl()
        );
        handleBinlogEvent(
                DataTable.of(CreativeIndex.class),
                creativeObject.getAdId(),
                creativeObject,
                type
        );
    }

    @SuppressWarnings("all")
    public static void handleLevel3(AdUnitTable unitTable, OpType type) {
        AdPlanObject planObject = DataTable.of(AdPlanIndex.class)
                .get(unitTable.getPlanId());
        if (planObject == null) {
            log.error("error! handleLevel3 found adPlanObject is not exist! {}", unitTable.getPlanId());
            return;
        }
        AdUnitObject unitObject = new AdUnitObject(
                unitTable.getUnitId(),
                unitTable.getUnitStatus(),
                unitTable.getPositionType(),
                unitTable.getPlanId(),
                planObject
        );
        handleBinlogEvent(
                DataTable.of(AdUnitIndex.class),
                unitObject.getUnitId(),
                unitObject,
                type
        );
    }

    public static void handleLevel3(AdCreativeUnitTable creativeUnitTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("CreativeUnitIndex not support update");
            return;
        }
        // 获取推广单元对象和创意对象
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(creativeUnitTable.getUnitId());
        CreativeObject creativeObject = DataTable.of(CreativeIndex.class).get(creativeUnitTable.getAdId());
        if (unitObject == null || creativeObject == null) {
            log.error("AdCreativeUnitTable index error, unitObject or creativeObject is null, {}",
                    JSON.toJSONString(creativeUnitTable));
            return;
        }
        CreativeUnitObject creativeUnitObject = new CreativeUnitObject(
                creativeUnitTable.getAdId(),
                creativeUnitTable.getUnitId()
        );
        handleBinlogEvent(
                DataTable.of(CreativeUnitIndex.class),
                CommonUtils.stringContact(
                        creativeUnitObject.getAdId().toString(),
                        creativeUnitObject.getUnitId().toString()
                ),
                creativeUnitObject,
                type
        );
    }

    public static void handleLevel4(AdUnitDistrictTable unitDistrictTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("districtIndex not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitDistrictTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitDistrictTable index error! unitObject is null: {}", unitDistrictTable.getUnitId());
            return;
        }
        String redisKey = CommonUtils.stringContact(
                unitDistrictTable.getProvince(),
                unitDistrictTable.getCity()
        );
        Set<Long> redisValue = new HashSet<>(
                Collections.singleton(unitDistrictTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitDistrictIndex.class),
                redisKey,
                redisValue,
                type
        );
    }

    public static void handleLevel4(AdUnitItTable unitItTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("it index not support update");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(unitItTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitItTable index error unitObject is null, {}", unitItTable.getUnitId());
            return;
        }
        Set<Long> redisValue = new HashSet<>(
                Collections.singleton(unitItTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitItIndex.class),
                unitItTable.getItTag(),
                redisValue,
                type
        );
    }

    public static void handleLevel4(AdUnitKeywordTable keywordTable, OpType type) {
        if (type == OpType.UPDATE) {
            log.error("keyword index update is not supported");
            return;
        }
        AdUnitObject unitObject = DataTable.of(AdUnitIndex.class).get(keywordTable.getUnitId());
        if (unitObject == null) {
            log.error("AdUnitKeywordTable index error: unitObject is not exist, {}", keywordTable.getUnitId());
            return;
        }
        Set<Long> redisValue = new HashSet<>(
                Collections.singleton(keywordTable.getUnitId())
        );
        handleBinlogEvent(
                DataTable.of(UnitKeywordIndex.class),
                keywordTable.getKeyword(),
                redisValue,
                type
        );
    }



    /**
     * 构造增量索引 或 加载全量索引
     *
     * @param indexAware 操作索引接口实现
     * @param key        索引key
     * @param value      索引 value
     * @param type       对于索引的操作 : 增删改
     */
    private static <K, V> void handleBinlogEvent(IIndexAware<K, V> indexAware, K key, V value, OpType type) {
        switch (type) {
            case ADD:
                indexAware.add(key, value);
                break;
            case DELETE:
                indexAware.delete(key, value);
                break;
            case UPDATE:
                indexAware.update(key, value);
                break;
            default:
                break;
        }
    }

}
