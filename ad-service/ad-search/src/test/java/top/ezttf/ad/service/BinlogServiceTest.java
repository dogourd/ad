package top.ezttf.ad.service;

import com.github.shyiko.mysql.binlog.BinaryLogClient;
import com.github.shyiko.mysql.binlog.event.DeleteRowsEventData;
import com.github.shyiko.mysql.binlog.event.EventData;
import com.github.shyiko.mysql.binlog.event.UpdateRowsEventData;
import com.github.shyiko.mysql.binlog.event.WriteRowsEventData;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.io.IOException;

/**
 * @author yuwen
 * @date 2019/1/30
 */
@Slf4j
@RunWith(BlockJUnit4ClassRunner.class)
public class BinlogServiceTest {

    @Test
    public void testBinlog() throws IOException {
        String hostname = "127.0.0.1", username = "yuwen", password = "lyp82nlf";
        int port = 3306;
        // BinaryLogClient其实就是一个连接数据库的客户端,
        // 它加自己伪装成slave 连接到master上
        BinaryLogClient client = new BinaryLogClient(
                hostname, port, username, password
        );
        // 设置监听的Binlog, 如果不设置则监听最新的Binlog
        //client.setBinlogFilename();
        // 设置监听的binlog位置, 如果不设置则监听最新的位置
        //client.setBinlogPosition();
        // 注册事件监听器, 监听期间MySQL发生的一些变化, Event代表已经发生的事件
        client.registerEventListener(event -> {
            // MySQL 数据表发生变化的一些数据
            EventData eventData = event.getData();
            if (eventData instanceof UpdateRowsEventData) {
                log.info("update event");
                log.debug("{}", eventData);
            } else if (eventData instanceof WriteRowsEventData) {
                log.info("write event");
                log.debug("{}", eventData);
            } else if (eventData instanceof DeleteRowsEventData) {
                log.info("delete event");
                log.debug("{}", eventData);
            }
        });
        // 连接到 master 开始监听
        client.connect();


        // 启动后手动连接到 MySQL执行
        // insert into `ad_unit_keyword` (`unit_id`, `keyword`) values (10, '标志');
        // 控制台得到日志
        // 15:39:17.410 [main] INFO top.ezttf.ad.service.BinlogServiceTest - write event
        // 15:39:17.459 [main] DEBUG top.ezttf.ad.service.BinlogServiceTest - WriteRowsEventData{tableId=122, includedColumns={0, 1, 2}, rows=[
        //     [13, 10, 标志]
        // ]}

        // WriteRowsEventData{tableId=118, includedColumns={0, 1, 2, 3, 4, 5, 6, 7}, rows=[
        //    [11, 666, plan, 1, 2019-01-01, 2019-01-01, Tue Jan 01 08:00:00 CST 2019, Tue Jan 01 08:00:00 CST 2019]
        //]}
    }
}
