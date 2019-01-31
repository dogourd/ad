package top.ezttf.ad.sender;

import top.ezttf.ad.mysql.dto.MySQLRowData;

/**
 * 投递增量数据
 *
 * @author yuwen
 * @date 2019/1/31
 */
public interface ISender {

    void sender(MySQLRowData rowData);

}
