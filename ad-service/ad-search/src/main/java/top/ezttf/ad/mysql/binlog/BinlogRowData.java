package top.ezttf.ad.mysql.binlog;

import com.github.shyiko.mysql.binlog.event.EventType;
import lombok.Data;
import top.ezttf.ad.mysql.dto.TableTemplate;

import java.util.List;
import java.util.Map;

/**
 * 抽取构造业务需要的mysql binlog实体
 *
 * @author yuwen
 * @date 2019/1/31
 */
@Data
public class BinlogRowData {

    private TableTemplate tableTemplate;

    private EventType eventType;

    /**
     * update 事件该值不为 null
     */
    private List<Map<String, String>> before;

    /**
     * Map中 key: columnName; value: columnValue
     */
    private List<Map<String, String>> after;
}
