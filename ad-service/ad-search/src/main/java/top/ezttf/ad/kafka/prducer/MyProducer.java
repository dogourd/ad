package top.ezttf.ad.kafka.prducer;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import top.ezttf.ad.kafka.partitioner.CustomPartitioner;

import java.util.Properties;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * @author yuwen
 * @date 2019/2/2
 */
@Slf4j
public class MyProducer {

    private static KafkaProducer<String, String> producer;

    static {
        Properties properties = new Properties();
        // broker list
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // 序列化key的类, 生产者发送key任意, 但是kafka只接受字节数组 需要实现kafka序列化接口
        properties.setProperty("key.serializer", "org.apache.kafka.common.serializer.StringSerializer");
        // value同上
        properties.setProperty("value.serializer", "org.apache.kafka.common.serializer.StringSerializer");

        properties.setProperty("partitioner.class", CustomPartitioner.class.getName());

        producer = new KafkaProducer<>(properties);
    }

    /** 最快*/
    private static void sendMessageForgetResult() {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                "kafka-topic",
                "key",
                "ignore result"
        );
        producer.send(record);
        producer.close();
    }

    /** 最慢 */
    private static void sendMessageSync() throws ExecutionException, InterruptedException {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                "kafka-topic",
                "key",
                "sync"
        );
        Future<RecordMetadata> result = producer.send(record);
        RecordMetadata metadata = result.get();

        log.debug("topic: {}", metadata.topic());
        log.debug("partition: {}", metadata.partition());
        log.debug("offset: {}", metadata.offset());
        producer.close();
    }

    /** 适中*/
    private static void sendMessageAsync() {
        ProducerRecord<String, String> record = new ProducerRecord<>(
                "kafka-topic",
                "key",
                "async"
        );
        producer.send(record, ((metadata, e) -> {
            log.info("coming into callback");
            if (e != null) {
                log.error("kafka exception: {}", e.getMessage());
                return;
            }
            log.debug("topic: {}", metadata.topic());
            log.debug("partition: {}", metadata.partition());
            log.debug("offset: {}", metadata.offset());
        }));
    }
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        sendMessageForgetResult();
        sendMessageSync();
        sendMessageAsync();
    }

}
