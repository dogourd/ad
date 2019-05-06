package top.ezttf.ad.client;

import org.springframework.stereotype.Component;
import top.ezttf.ad.client.vo.AdPlan;
import top.ezttf.ad.client.vo.AdPlanGetRequest;
import top.ezttf.ad.constant.Constants;
import top.ezttf.ad.enums.ResponseCode;
import top.ezttf.ad.vo.CommonResponse;

import java.util.List;

/**
 * sponsor client 断路器
 *
 * 定义一个 hystrix方法非常简单, 只需要去实现定义的 {@link ISponsorClient}接口然后实现对应的方法
 * 同时将hystrix绑定到{@link ISponsorClient}就可以实现短路。
 * 绑定方式即在接口中{@link @FeignClient}指定fallback属性为该类的class即可
 * 一旦调用ad-sponsor的过程中发生错误
 * 就会做服务降级, 就会实现到本类中的方法, 返回相应的结果
 * @author yuwen
 * @date 2019/1/25
 */
@Component
public class SponsorClientHystrix implements ISponsorClient{
    @Override
    public CommonResponse<List<AdPlan>> getAdPlanList(AdPlanGetRequest request) {

        return new CommonResponse<>(ResponseCode.ERROR.getCode(), Constants.ErrorMsg.EUREKA_CLIENT_AD_SPONSOR_ERROR);
    }
}
