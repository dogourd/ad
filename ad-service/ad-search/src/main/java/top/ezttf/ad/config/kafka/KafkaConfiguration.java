package top.ezttf.ad.config.kafka;

import com.google.common.collect.Maps;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ConcurrentMessageListenerContainer;

import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * @author yuwen
 * @date 2019/5/30
 */
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private List<String> bootstrapServers;
    @Value("${spring.kafka.listener.concurrency}")
    private Integer concurrency;

    @Bean
    KafkaListenerContainerFactory<ConcurrentMessageListenerContainer<String, String>> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> containerFactory = new ConcurrentKafkaListenerContainerFactory<>();
        containerFactory.setConcurrency(concurrency);

        Map<String, Object> config = Maps.newHashMap();
        config.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        config.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        config.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        // 由于课程原版实现中广告的索引数据是存在于ConcurrentHashMap中, 即每个索引服务实例的jvm中。
        // 所以当每一个索引实例监听kafka topic数据时, 需要保证每个实例都处于不同的消费者组
        // 即各个实例之间需要各不相同的groupId, 保证在部署多实例时, 每个实例都可以加载到完整的索引数据

        // 但在本实现中由于将索引数据单独提出, 存放到了Redis数据库中, 所以应该让所有实例属于同一个消费者组
        // 共同消费kafka topic下的数据, 保证索引数据不会被重复消费。

        // 综上, 若索引数据的存放如果为各个实例自身的jvm, 应该考虑加上以下代码(或自行编写其他实现)保证各实例处于不同的消费者组
        // 若索引数据存放的位置, 是所有检索实例共享的位置, 应该将以下配置取消(或直接删除本类)
        config.put(ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString());
        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<String, String>(config);
        containerFactory.setConsumerFactory(consumerFactory);
        return containerFactory;
    }
}
