package top.ezttf.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.*;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ezttf.ad.dto.TableTemplate;
import top.ezttf.ad.mysql.TemplateHolder;
import top.ezttf.ad.mysql.binlog.BinlogRowData;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 数据收集 聚合到mysql 的 binlog
 *
 * @author yuwen
 * @date 2019/1/31
 */
@Slf4j
@Component
public class AggregationListener implements BinaryLogClient.EventListener {

    private String databaseName;
    private String tableName;

    /**
     * 存储 表名 ---> 监听器
     *
     * 同一个监听器 可以多次实现监听不同的数据库, 数据表
     * 数据库的每张表的监听器也都可以监听不同的事件
     */
    private Map<String, IListener> listenerMap = Maps.newHashMap();

    // 载入模板信息
    private final TemplateHolder templateHolder;

    @Autowired
    public AggregationListener(TemplateHolder templateHolder) {
        this.templateHolder = templateHolder;
    }


    private String generateKey(String databaseName, String tableName) {
        return databaseName + ":" + tableName;
    }


    public void register(String databaseName, String tableName, IListener iListener) {
        log.info("register : {}-{}", databaseName, tableName);
        this.listenerMap.put(generateKey(databaseName, tableName), iListener);
    }


    /**
     * 实现 mysql-binlog-connector-java中提供的事件监听方法 对事件进行监听
     * 1. 对于mysql发生的任意事件, 记录下来事件对应的数据库和数据表
     * 2. 判断. 实际的业务逻辑是否需要关心该事件, 若关心则继续进行, 否则直接跳过并返回
     * 3. 拿到监听到的事件中的数据{@link EventData} 对其进行包装, 转化为业务逻辑自定义实体 {@link BinlogRowData}
     * 4. 对于包装后的 {@link BinlogRowData} 使用自定义的 业务事件监听器{@link IListener} 进行处理, 对于检索服务来说
     * 对{@link BinlogRowData}的处理方式就是实现对增量数据的一次更新
     *
     * @param event
     */
    @Override
    public void onEvent(Event event) {
        EventType eventType = event.getHeader().getEventType();
        log.debug("event  type : {}", eventType);
        if (eventType == EventType.TABLE_MAP) {
            // table_map中包含了接下来要操作的数据库和数据表
            TableMapEventData eventData = event.getData();
            this.tableName = eventData.getTable();
            this.databaseName = eventData.getDatabase();
            return;
        }
        if (!EventType.isRowMutation(eventType)) {
            return;
        }
        // 判断表名和库名是否已经完成填充, 因为已经在第一步判断 TABLE_MAP中尝试填充 databaseName 和 tableName
        if (StringUtils.isBlank(databaseName) || StringUtils.isBlank(tableName)) {
            log.error("no meta data event");
        }
        // 找出对应表有兴趣的监听器
        String key = generateKey(databaseName, tableName);
        IListener iListener = this.listenerMap.get(key);
        if (iListener == null) {
            // 表明没有关心该表的监听器
            log.debug("skip {}", key);
            return;
        }
        log.info("trigger event: {}", eventType.name());
        try {
            // 预将参数中的Event中的EventData属性转变为自定义的BinlogRowData
            BinlogRowData rowData = buildRowData(event.getData());
            if (rowData == null) {
                return;
            }
            rowData.setEventType(eventType);
            // 用自定义的事件处理方法, 去对数据进行处理
            iListener.onEvent(rowData);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        } finally {
            this.databaseName = StringUtils.EMPTY;
            this.tableName = StringUtils.EMPTY;
        }
    }


    /**
     * 一次单行insert事件后, 产生的eventData如下
     * WriteRowsEventData{tableId=122, includedColumns={0, 1, 2}, rows=[[13, 10, 标志]]},
     * update, delete事件可以具体测试或查阅官方文档
     *
     * @param eventData
     * @return
     */
    private BinlogRowData buildRowData(EventData eventData) {
        TableTemplate tableTemplate = templateHolder.getTable(this.tableName);
        if (tableTemplate == null) {
            // template.json中未指定该表, 或者templateHolder init阶段出现故障
            log.warn("table not found");
            return null;
        }
        BinlogRowData rowData = new BinlogRowData();
        if (eventData instanceof UpdateRowsEventData) {
            List<Map<String, String>> beforeMapList = new ArrayList<>();
            List<Serializable[]> beforeValues = ((UpdateRowsEventData) eventData).getRows().stream()
                    .map(Map.Entry::getKey)
                    .collect(Collectors.toList());
            for (Serializable[] beforeValue : beforeValues) {
                Map<String, String> beforeMap = new HashMap<>();
                int length = beforeValue.length;
                for (int i = 0; i < length; i++) {
                    // 字段索引   对应到   字段名
                    String columnName = tableTemplate.getPositionMap().get(i);
                    if (StringUtils.isBlank(columnName)) {
                        // template.json中未定义该列, 即不关心该列的变化
                        log.debug("ignore position: {}", i);
                        continue;
                    }
                    String columnValue = beforeValue[i].toString();
                    beforeMap.put(columnName, columnValue);
                    log.debug("before update: {}", beforeMap);
                }
                beforeMapList.add(beforeMap);
            }
            rowData.setBefore(beforeMapList);
        }
        List<Map<String, String>> afterMapList = new ArrayList<>();
        for (Serializable[] afterValue : getAfterValues(eventData)) {
            Map<String, String> afterMap = new HashMap<>(afterValue.length);
            int length = afterValue.length;
            for (int i = 0; i < length; i++) {
                String columnName = tableTemplate.getPositionMap().get(i);
                if (StringUtils.isBlank(columnName)) {
                    // template.json中未定义该列, 即不关心该列的变化
                    log.debug("ignore position: {}", i);
                    continue;
                }
                String columnValue = afterValue[i].toString();
                afterMap.put(columnName, columnValue);
            }
            afterMapList.add(afterMap);
        }
        rowData.setAfter(afterMapList);
        rowData.setTableTemplate(tableTemplate);
        return rowData;
    }


    private List<Serializable[]> getAfterValues(EventData eventData) {
        if (eventData instanceof WriteRowsEventData) {
            // insert event
            return ((WriteRowsEventData) eventData).getRows();
        }
        if (eventData instanceof UpdateRowsEventData) {
            // update event
            return ((UpdateRowsEventData) eventData).getRows().stream()
                    .map(Map.Entry::getValue)
                    .collect(Collectors.toList());
        }
        if (eventData instanceof DeleteRowsEventData) {
            // delete event
            return ((DeleteRowsEventData) eventData).getRows();
        }
        // else ==== ignore
        return Collections.emptyList();
    }
}
