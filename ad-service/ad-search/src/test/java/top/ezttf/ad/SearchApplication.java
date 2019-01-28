package top.ezttf.ad;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.kafka.KafkaAutoConfiguration;

/**
 * @author yuwen
 * @date 2019/1/25
 */
@SpringBootApplication(exclude = {
        KafkaAutoConfiguration.class
})
public class SearchApplication {
}
