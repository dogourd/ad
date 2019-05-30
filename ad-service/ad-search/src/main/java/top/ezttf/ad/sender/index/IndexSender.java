package top.ezttf.ad.sender.index;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import top.ezttf.ad.constant.DBConstant;
import top.ezttf.ad.dto.MysqlRowData;
import top.ezttf.ad.dump.table.*;
import top.ezttf.ad.handler.AdLevelDataHandler;
import top.ezttf.ad.index.DataLevel;
import top.ezttf.ad.sender.ISender;
import top.ezttf.ad.util.CommonUtils;

import java.util.List;

/**
 * 广告增量数据的投放数据(该类实现 调用AdLevelDataHandler将索引数据保存在redis中)
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Slf4j
@Component(value = "indexSender")
public class IndexSender implements ISender {

    /**
     * 根据不同的数据层级完成 增量数据投递
     *
     * @param rowData
     */
    @Override
    public void sender(MysqlRowData rowData) {
        String level = rowData.getLevel();
        if (DataLevel.LEVEL_TWO.getLevel().equals(level)) {
            level2RowData(rowData);
        } else if (DataLevel.LEVEL_THREE.getLevel().equals(level)) {
            level3RowData(rowData);
        } else if (DataLevel.LEVEL_FOUR.getLevel().equals(level)) {
            level4RowData(rowData);
        } else {
            log.error("mysqlRowData error: {}", JSON.toJSONString(rowData));
        }
    }

    /**
     * 第二层级增量数据投递
     * ad_plan         推广计划
     * ad_creative     创意
     *
     * @param rowData
     */
    private void level2RowData(MysqlRowData rowData) {
        if (rowData.getTableName().equals(DBConstant.AD_PLAN_TABLE_INFO.TABLE_NAME)) {
            List<AdPlanTable> planTableList = Lists.newArrayList();
            rowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdPlanTable planTable = new AdPlanTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_ID:
                            planTable.setId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_USER_ID:
                            planTable.setUserId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_PLAN_STATUS:
                            planTable.setPlanStatus(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_START_DATE:
                            planTable.setStartDate(CommonUtils.parseStringLocalDateTime(columnValue));
                            break;
                        case DBConstant.AD_PLAN_TABLE_INFO.COLUMN_END_DATE:
                            planTable.setEndDate(CommonUtils.parseStringLocalDateTime(columnValue));
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                planTableList.add(planTable);
            });
            planTableList.forEach(planTable -> {
                AdLevelDataHandler.handleLevel2(planTable, rowData.getOpType());
            });
        } else if (rowData.getTableName().equals(DBConstant.AD_CREATIVE_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeTable> creativeTables = Lists.newArrayList();
            rowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdCreativeTable creativeTable = new AdCreativeTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_ID:
                            creativeTable.setAdId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_TYPE:
                            creativeTable.setType(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_MATERIAL_TYPE:
                            creativeTable.setMaterialType(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_AUDIT_STATUS:
                            creativeTable.setAuditStatus(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_height:
                            creativeTable.setHeight(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_width:
                            creativeTable.setWidth(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_TABLE_INFO.COLUMN_URL:
                            creativeTable.setAdUrl(columnValue);
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                creativeTables.add(creativeTable);
            });
            creativeTables.forEach(creativeTable -> {
                AdLevelDataHandler.handleLevel2(creativeTable, rowData.getOpType());
            });
        }
    }

    /**
     * 第三层级 增量数据投递
     * ad_unit          推广单元
     * creative_unit    退广单元-创意关联
     *
     * @param rowData
     */
    private void level3RowData(MysqlRowData rowData) {
        if (rowData.getTableName().equals(DBConstant.AD_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdUnitTable> unitTables = Lists.newArrayList();
            rowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitTable unitTable = new AdUnitTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_ID:
                            unitTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_PLAN_ID:
                            unitTable.setPlanId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_UNIT_STATUS:
                            unitTable.setUnitStatus(Integer.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_TABLE_INFO.COLUMN_POSITION_TYPE:
                            unitTable.setPositionType(Integer.valueOf(columnValue));
                            break;
                        default:
                            log.warn("not matched column, ignored");
                            break;
                    }
                });
                unitTables.add(unitTable);
            });
            unitTables.forEach(unitTable -> {
                AdLevelDataHandler.handleLevel3(unitTable, rowData.getOpType());
            });
        } else if (rowData.getTableName().equals(DBConstant.AD_CREATIVE_UNIT_TABLE_INFO.TABLE_NAME)) {
            List<AdCreativeUnitTable> creativeUnitTables = Lists.newArrayList();
            rowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdCreativeUnitTable creativeUnitTable = new AdCreativeUnitTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_CREATIVE_ID:
                            creativeUnitTable.setAdId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_CREATIVE_UNIT_TABLE_INFO.COLUMN_UNIT_ID:
                            creativeUnitTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                creativeUnitTables.add(creativeUnitTable);
            });
            creativeUnitTables.forEach(creativeUnitTable -> {
                AdLevelDataHandler.handleLevel3(creativeUnitTable, rowData.getOpType());
            });
        }
    }

    /**
     * 第四层级 增量数据投递
     * ad_unit_it           推广单元 - 兴趣关联
     * ad_unit_district     推广单元 - 地域关联
     * ad_unit_keyword      推广单元 - 关键词关联
     * @param rowData
     */
    private void level4RowData(MysqlRowData rowData) {
        if (rowData.getTableName().equals(DBConstant.AD_UNIT_IT_TABLE_INFO.TABLE_NAME)) {
            rowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitItTable unitItTable = new AdUnitItTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_IT_TABLE_INFO.COLUMN_UNIT_ID:
                            unitItTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_IT_TABLE_INFO.COLUMN_IT_TAG:
                            unitItTable.setItTag(columnValue);
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                AdLevelDataHandler.handleLevel4(unitItTable, rowData.getOpType());
            });
        } else if (rowData.getTableName().equals(DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.TABLE_NAME)) {
            rowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitDistrictTable unitDistrictTable = new AdUnitDistrictTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_UNIT_ID:
                            unitDistrictTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_PROVINCE:
                            unitDistrictTable.setProvince(columnValue);
                            break;
                        case DBConstant.AD_UNIT_DISTRICT_TABLE_INFO.COLUMN_CITY:
                            unitDistrictTable.setCity(columnValue);
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                AdLevelDataHandler.handleLevel4(unitDistrictTable, rowData.getOpType());
            });
        } else if (rowData.getTableName().equals(DBConstant.AD_UNIT_KEYWORD_TABLE_INFO.TABLE_NAME)) {
            rowData.getFieldValueMapList().forEach(fieldValueMap -> {
                AdUnitKeywordTable unitKeywordTable = new AdUnitKeywordTable();
                fieldValueMap.forEach((columnName, columnValue) -> {
                    switch (columnName) {
                        case DBConstant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_UNIT_ID:
                            unitKeywordTable.setUnitId(Long.valueOf(columnValue));
                            break;
                        case DBConstant.AD_UNIT_KEYWORD_TABLE_INFO.COLUMN_KEYWORD:
                            unitKeywordTable.setKeyword(columnValue);
                            break;
                        default:
                            log.warn("not match a valid column, ignored");
                            break;
                    }
                });
                AdLevelDataHandler.handleLevel4(unitKeywordTable, rowData.getOpType());
            });
        }
    }
}
