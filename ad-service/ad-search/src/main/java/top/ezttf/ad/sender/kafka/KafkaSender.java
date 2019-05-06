package top.ezttf.ad.sender.kafka;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.mysql.dto.MysqlRowData;
import top.ezttf.ad.sender.ISender;

import java.util.Optional;

/**
 * 将增量数据投放到 kafka
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Slf4j
@Component(value = "kafkaSender")
public class KafkaSender implements ISender {

    @Value(value = "${adconf.kafka.topic}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public KafkaSender(KafkaTemplate<String, String> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    /**
     * 将rowData序列化为Json投放到 kafka topic中
     * @param rowData
     */
    @Override
    public void sender(MysqlRowData rowData) {
        kafkaTemplate.send(topic, JSON.toJSONString(rowData));
    }

    @KafkaListener(
            topics = {"${adconf.kafka.topic}"}, groupId = "ad-search"
    )
    public void processMysqlRowData(ConsumerRecord<?, ?> record) {
        Optional<?> kafkaMessage = Optional.ofNullable(record.value());
        if (kafkaMessage.isPresent()) {
            Object message = kafkaMessage.get();
            // 将topic中的消息反序列化回rowData
            MysqlRowData rowData = JSON.parseObject(message.toString(), MysqlRowData.class);
            log.debug("kafka processMysqlRowData: {}", JSON.toJSONString(rowData));
        }
    }
}
