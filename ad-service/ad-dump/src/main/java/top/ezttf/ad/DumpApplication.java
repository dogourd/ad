package top.ezttf.ad;

import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @author yuwen
 * @date 2019/1/28
 */
@EnableCircuitBreaker
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class DumpApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(DumpApplication.class)
                .web(WebApplicationType.NONE)
                .run(args);
    }
}


