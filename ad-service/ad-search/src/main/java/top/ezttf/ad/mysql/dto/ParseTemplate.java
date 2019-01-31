package top.ezttf.ad.mysql.dto;

import lombok.Data;
import top.ezttf.ad.mysql.constant.OpType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

/**
 * ParseTemplate{
 *     databaseName
 *     tableTemplateMap[
 *          tableName --> TableTemplate{
 *                    tableName,
 *                    level,
 *                    opType->columnNameList
 *                    columnIndexList --> ColumnNameList
 *          }
 *     ]
 * }
 *
 * @author yuwen
 * @date 2019/1/30
 */
@Data
public class ParseTemplate {

    private String databaseName;

    /**
     * key    : 表名
     * value  : 表属性
     */
    private Map<String, TableTemplate> tableTemplateMap = new HashMap<>();


    /**
     * 对照类图查看
     * 1. 获取{@link databaseName}
     *    直接从JsonTemplate的databaseName属性中获取 dbName, 直接作为{@link ParseTemplate}的databaseName属性值;
     * 2. 构造 {@link tableTemplateMap}
     *    (1) opTypeFieldSetMap
     *    从jsonTemplate中获取List<JsonTable>进行逐个遍历,
     *    获取{@link JsonTable}的insertList, updateList以及deleteList(即需要增删改的列),
     *    并将其分别作为value, 将操作类型{@link OpType}为key, 封装为map作为{@link TableTemplate} 中的opTypeFieldSetMap属性
     *
     *    (2) level
     *    在(1)中对List<JsonTable>遍历过程中直接提取 level属性转为String作为 {@link TableTemplate} 中的 level属性
     *
     *    (3) tableName
     *    同 (2), 可以直接在对List<JsonTable>遍历过程中直接提取 tableName作为 {@link TableTemplate} 的tableName属性
     *
     *    (4) positionMap
     *    未处理, 因为该属性代表数据库的列索引和列名的映射, 该属性的填充需要获取数据库的 information_schema.column,
     *    交由以后处理
     *
     * 步骤二遍历过程中可以从每一个 {@link JsonTable}中提取出来一个未处理 positionMap属性的 {@link TableTemplate}, 一次遍历结束后
     * 将构造出来的{@link TableTemplate} 作为value, tableName作为Key, 封装为map作为ParseTemplate中的tableTemplateMap属性
     * @param jsonTemplate
     * @return
     */
    public static ParseTemplate parse(Template jsonTemplate) {
        ParseTemplate template = new ParseTemplate();
        template.setDatabaseName(jsonTemplate.getDatabaseName());
        for (JsonTable table : jsonTemplate.getTableList()) {
            String tableName = table.getTableName();
            Integer tableLevel = table.getLevel();
            TableTemplate tableTemplate = new TableTemplate();
            tableTemplate.setTableName(tableName);
            tableTemplate.setLevel(tableLevel.toString());
            template.getTableTemplateMap().put(tableName, tableTemplate);
            // 遍历操作类型对应的列
            Map<OpType, List<String>> opTypeFieldSetMap = tableTemplate.getOpTypeFieldSetMap();
            table.getInsertList().forEach(column -> {
                getAndCreateIfNeed(
                        OpType.ADD,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumnName());
            });
            table.getUpdateList().forEach(column -> {
                getAndCreateIfNeed(
                        OpType.UPDATE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumnName());
            });
            table.getDeleteList().forEach(column -> {
                getAndCreateIfNeed(
                        OpType.DELETE,
                        opTypeFieldSetMap,
                        ArrayList::new
                ).add(column.getColumnName());
            });
        }
        return template;
    }


    private static<T, R> R getAndCreateIfNeed(T key, Map<T, R> map, Supplier<R> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }
}
