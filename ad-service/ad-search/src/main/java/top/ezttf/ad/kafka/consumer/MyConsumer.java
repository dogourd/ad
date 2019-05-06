package top.ezttf.ad.kafka.consumer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.consumer.CommitFailedException;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;

import java.util.Collections;
import java.util.Properties;

/**
 * @author yuwen
 * @date 2019/2/3
 */
@Slf4j
public class MyConsumer {

    private static KafkaConsumer<String, String> consumer;
    private static Properties properties = new Properties();

    static {
        properties.setProperty("bootstrap.servers", "127.0.0.1:9092");
        // 消费消息应该是消息的反序列化
        properties.setProperty("key.deserializer", StringDeserializer.class.getName());
        properties.setProperty("value.deserializer", StringDeserializer.class.getName());
        properties.setProperty("group.id", "consumer-group-1");
    }

    /**
     * 自动提交消息位移
     */
    private static void generalConsumerMessageAutoCommit() {
        // 消费者会在 poll方法后每隔 5 秒提交poll后的最大位移
        properties.put("enable.auto.commit", "true");
        consumer = new KafkaConsumer<>(properties);
        // 消费者订阅 topic
        consumer.subscribe(Collections.singleton("kafka-topic"));
        // 循环拉取 kafka数据
        try {
            while (true) {
                boolean flag = true;
                // 拉取消息, 并设置超时时间
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    log.debug(String.format("topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(), record.key(), record.value())
                    );
                    if (StringUtils.endsWithIgnoreCase("done", record.value())) {
                        flag = false;
                    }
                }
                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 手动同步提交消息位移
     */
    private static void generalConsumerMessageSyncCommit() {
        properties.put("auto.commit.offset", "false");
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("kafka-topic"));
        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    log.debug(String.format("topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(), record.key(), record.value())
                    );
                    if (StringUtils.endsWithIgnoreCase("done", record.value())) {
                        flag = false;
                    }
                }
                try {
                    // 发起提交, 当前线程会阻塞, 如果发生异常会进行重试直到成功或者抛出 CommitFailedException
                    consumer.commitSync();
                } catch (CommitFailedException e) {
                    log.error("commit failed error: {}", e.getMessage());
                }
                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 手动异步提交消息位移
     */
    private static void generalConsumerMessageAsyncCommit() {

        properties.put("auto.commit.offset", "false");
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("kafka-topic"));
        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    log.debug(String.format("topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(), record.key(), record.value())
                    );
                    if (StringUtils.endsWithIgnoreCase("done", record.value())) {
                        flag = false;
                    }
                }
                // 异步提交最新消息位移, 不会发生阻塞, 但是如果服务器返回提交失败, 异步提交不会进行重试
                // 因为如果同时存在多个异步提交, 进行重试可能会导致位移的覆盖
                // commit A, offset 2000
                // commit B, offset 3000
                // 若A 提交失败, 但是 B提交成功, 如果A重试成功则会将3000 回滚到2000导致消息重复消费
                consumer.commitAsync();
                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }

    }

    /**
     * 手动异步提交带回调函数
     */
    private static void generalConsumerMessageAsyncCommitWithCallback() {
        properties.put("auto.commit.offset", "false");
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("kafka-topic"));
        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    log.debug(String.format("topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(), record.key(), record.value())
                    );
                    if (StringUtils.endsWithIgnoreCase("done", record.value())) {
                        flag = false;
                    }
                }
                consumer.commitAsync((offsets, exception) -> {
                    // 即使捕获到异常, 也不应该重复尝试提交
                    if (exception != null) {
                        log.error("commit failed for offsets: {}", exception.getMessage());
                        offsets.forEach((topicPartition, offsetAndMetadata) ->
                                log.error("topic: {}, offset: {}, Metadata: {}", topicPartition.topic(),
                                        offsetAndMetadata.offset(), offsetAndMetadata.metadata())
                        );
                    }
                });
                if (!flag) {
                    break;
                }
            }
        } finally {
            consumer.close();
        }
    }

    /**
     * 混合使用同步提交和异步提交
     * 若之前的提交失败, 则那次提交的位移号一定是偏小, 靠后的提交位移号必定偏大一些
     */
    private static void mixSyncAndAsyncCommit() {
        properties.put("auto.commit.offset", "false");
        consumer = new KafkaConsumer<>(properties);
        consumer.subscribe(Collections.singleton("kafka-topic"));
        try {
            while (true) {
                boolean flag = true;
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    log.debug(String.format("topic = %s, partition = %s, key = %s, value = %s",
                            record.topic(), record.partition(), record.key(), record.value())
                    );
                    if (StringUtils.endsWithIgnoreCase("done", record.value())) {
                        flag = false;
                    }
                }
                consumer.commitAsync();
                if (!flag) {
                    break;
                }
            }
        } catch (Exception e) {
            log.error("commit async error: {}", e.getMessage());
        } finally {
            try {
                consumer.commitSync();
            } finally {
                consumer.close();
            }
        }
    }


    public static void main(String[] args) {
        generalConsumerMessageAutoCommit();
        generalConsumerMessageSyncCommit();
        generalConsumerMessageAsyncCommit();
        generalConsumerMessageAsyncCommitWithCallback();
        mixSyncAndAsyncCommit();
    }
}
