package top.ezttf.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * @author yuwen
 * @date 2019/1/24
 */
@EnableFeignClients                             // 使用feign去访问其他的微服务
@EnableEurekaClient                             // 作为eureka client注册到 eureka 注册中心
@EnableHystrix                                  // 使用断路器
@EnableCircuitBreaker                           // 也是使用断路器
@EnableDiscoveryClient                          // 开启微服务的发现能力
@EnableHystrixDashboard                         // hystrix 一些监控
@SpringBootApplication
public class SearchApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringApplication.class, args);
    }

    @Bean
    @LoadBalanced                   // 开启负载均衡的功能
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
