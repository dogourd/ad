package top.ezttf.ad.kafka.partitioner;

import org.apache.commons.lang.StringUtils;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.apache.kafka.common.PartitionInfo;
import org.apache.kafka.common.record.InvalidRecordException;
import org.apache.kafka.common.utils.Utils;

import java.util.List;
import java.util.Map;

/**
 * 自定义消息分配器
 * 预使用自定义分配器 需要将其配置在{@link org.apache.kafka.clients.producer.KafkaProducer}的
 * {@link java.util.Properties}中 {@code properties.put("partitioner.class", CustomPartitioner.class.getName())}
 *
 * @author yuwen
 * @date 2019/2/3
 */
public class CustomPartitioner implements Partitioner {

    /**
     * 决定消息被写入哪个分区
     * @param topic topic
     * @param key key
     * @param keyBytes key serialize byte
     * @param value value
     * @param valueBytes value serialize byte
     * @param cluster kakfa cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes,
                         Object value, byte[] valueBytes, Cluster cluster) {
        // 所有分区信息
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        int partitionCount = partitionInfos.size();
        // 要求必须存在 key,如果key 是"name" 就分配到最后一个分区, 其他key hash取模
        if (keyBytes == null || !key.getClass().equals(String.class)) {
            throw new InvalidRecordException("kafka message must have a String key");
        }
        if (partitionCount == 1 || StringUtils.endsWithIgnoreCase("name", key.toString())) {
            return partitionCount - 1;
        }
        return Math.abs(Utils.murmur2(keyBytes)) % (partitionCount - 1);
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
