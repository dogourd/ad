package top.ezttf.ad;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;

/**
 * @author yuwen
 * @date 2019/2/13
 */
@EnableEurekaClient
@EnableHystrixDashboard
@SpringBootApplication
public class DashboardApplication {


    public static void main(String[] args) {
        SpringApplication.run(DashboardApplication.class, args);
    }
}
