package top.ezttf.ad.mysql.constant;

import com.github.shyiko.mysql.binlog.event.EventType;

/**
 * @author yuwen
 * @date 2019/1/29
 */
public enum OpType {

    ADD,
    UPDATE,
    DELETE,
    OTHER;

    public static OpType to(EventType eventType) {
        if (EventType.isUpdate(eventType)) {
            return UPDATE;
        }
        if (EventType.isWrite(eventType)) {
            return ADD;
        }
        if (EventType.isDelete(eventType)) {
            return DELETE;
        }
        return OTHER;
    }
}
