package top.ezttf.ad.consumer;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.stereotype.Component;
import top.ezttf.ad.dto.MysqlRowData;
import top.ezttf.ad.sender.ISender;

import java.util.Optional;

/**
 * 消费 kafka binlog message
 * topic : ad-search-mysql-data
 *
 * @author yuwen
 * @date 2019/5/30
 */
@Slf4j
@Component
public class BinlogConsumer {

    private final ISender iSender;
    private final KafkaListenerContainerFactory containerFactory;

    public BinlogConsumer(ISender iSender, KafkaListenerContainerFactory containerFactory) {
        this.iSender = iSender;
        this.containerFactory = containerFactory;
    }


    /**
     * 监听topic: ad-search-mysql-data, 从kafka中获取由ad-binlog-kafka服务投递的增量索引数据,
     * 获取到增量数据后通过{@link top.ezttf.ad.sender.index.IndexSender}将增量数据投放的Redis中
     * @param record
     */
    @KafkaListener(topics = {"${adconf.kafka.topic}"})
    public void processMySQLRowData(ConsumerRecord<?, ?> record) {
        Optional<?> optional = Optional.ofNullable(record.value());
        if (optional.isPresent()) {
            Object kafkaMessage = optional.get();
            MysqlRowData rowData = JSON.parseObject(kafkaMessage.toString(), MysqlRowData.class);
            log.info("kafka processMySQLRowData: {}", JSON.toJSONString(rowData));
            iSender.sender(rowData);
        }
    }
}
