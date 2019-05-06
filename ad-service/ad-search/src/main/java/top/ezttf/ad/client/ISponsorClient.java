package top.ezttf.ad.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import top.ezttf.ad.client.vo.AdPlan;
import top.ezttf.ad.client.vo.AdPlanGetRequest;
import top.ezttf.ad.vo.CommonResponse;

import java.util.List;

/**
 * 使用feign 进行微服务调用
 * 需注意: 使用Feign 需要使用到 @EnableFeignClients注解(此注解已标注在启动类当中),
 *
 * 一旦服务不可用, 则产生服务降级, 去调用{@link SponsorClientHystrix}中的方法实现
 * @author yuwen
 * @date 2019/1/25
 */
@FeignClient(value = "eureka-client-ad-sponsor", fallback = SponsorClientHystrix.class)
public interface ISponsorClient {

    @PostMapping(value = "/ad-sponsor/get/adPlan")
    CommonResponse<List<AdPlan>> getAdPlanList(@RequestBody AdPlanGetRequest request);
}
