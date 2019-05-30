package top.ezttf.ad.mysql.listener;

import com.github.shyiko.mysql.binlog.event.EventType;
import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import top.ezttf.ad.constant.DBConstant;
import top.ezttf.ad.constant.OpType;
import top.ezttf.ad.dto.MysqlRowData;
import top.ezttf.ad.dto.TableTemplate;
import top.ezttf.ad.mysql.binlog.BinlogRowData;
import top.ezttf.ad.sender.ISender;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/31
 */
@Slf4j
@Component
public class IncrementListener implements IListener {

    /**
     * {@link top.ezttf.ad.sender.kafka.KafkaSender}
     */
    private final ISender iSender;
    private final AggregationListener aggregationListener;

    @Autowired
    public IncrementListener(
            AggregationListener aggregationListener,
            ISender iSender) {
        this.aggregationListener = aggregationListener;
        this.iSender = iSender;
    }
    /**
     * 注册需要处理的表
     */
    @Override
    @PostConstruct
    public void register() {
        log.info("IncrementListener register db as table info");
        DBConstant.TABLE_2_DB.forEach((key, value) -> aggregationListener.register(value, key, this));
    }

    /**
     * 事件处理逻辑, 投放增量数据
     * @param eventData binlog监听到的数据, 许哟啊对其解析
     */
    @Override
    public void onEvent(BinlogRowData eventData) {
        TableTemplate tableTemplate = eventData.getTableTemplate();
        EventType eventType = eventData.getEventType();

        // 将数据包装为需要投递的数据格式 MySQLRowData
        MysqlRowData mySQLRowData = new MysqlRowData();
        mySQLRowData.setTableName(tableTemplate.getTableName());
        mySQLRowData.setLevel(tableTemplate.getLevel());
        mySQLRowData.setOpType(OpType.to(eventType));

        // 取出模板中操作对应的字段列表
        List<String> fieldList = tableTemplate.getOpTypeFieldSetMap().get(mySQLRowData.getOpType());
        if (fieldList == null) {
            // template.json中表示的表中不存在该操作类型。数据不应该被处理
            log.warn("{} not support for {}", mySQLRowData.getOpType(), mySQLRowData.getTableName());
            return;
        }
        eventData.getAfter().forEach(afterMap ->
                mySQLRowData.getFieldValueMapList().add(Maps.newHashMap())
        );
        iSender.sender(mySQLRowData);
    }
}
