package top.ezttf.ad.mysql.listener;

import top.ezttf.ad.mysql.binlog.BinlogRowData;

/**
 * @author yuwen
 * @date 2019/1/31
 */
public interface IListener {

    /**
     * 可以对不同的表进行不同的增量数据更新方法, 可以通过此方法注册不同的监听器
     */
    void register();

    /**
     * 监听到事件
     * @param eventData
     */
    void onEvent(BinlogRowData eventData);

}
