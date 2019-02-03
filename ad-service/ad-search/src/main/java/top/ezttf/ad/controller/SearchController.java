package top.ezttf.ad.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import top.ezttf.ad.annotation.IgnoreResponseAdvice;
import top.ezttf.ad.client.ISponsorClient;
import top.ezttf.ad.client.vo.AdPlan;
import top.ezttf.ad.client.vo.AdPlanGetRequest;
import top.ezttf.ad.search.ISearch;
import top.ezttf.ad.search.vo.SearchRequest;
import top.ezttf.ad.search.vo.SearchResponse;
import top.ezttf.ad.vo.CommonResponse;

import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/24
 */
@Slf4j
@RestController
public class SearchController {

    private final ISearch iSearch;

    private final RestTemplate restTemplate;

    private final ISponsorClient iSponsorClient;
    @Autowired
    public SearchController(RestTemplate restTemplate, ISponsorClient iSponsorClient, ISearch iSearch) {
        this.restTemplate = restTemplate;
        this.iSponsorClient = iSponsorClient;
        this.iSearch = iSearch;
    }


    /**
     * 考虑一个问题, 假如ad-sponsor服务已经下线
     * 那么调用的时候必然后抛出错误, 随着错误不断抛出, 级联的错误可能会引起雪崩
     * 此时可以引入 spring-cloud 提供的另一个组件hystrix ,断路器
     * @param request
     * @return
     */
    @IgnoreResponseAdvice
    @PostMapping("/getAdPlanListByFeign")
    public CommonResponse<List<AdPlan>> getAdPlanListByFeign(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlanByFeign -> {}", JSON.toJSONString(request));
        return iSponsorClient.getAdPlanList(request);
    }

    @PostMapping("/getAdPlanListByRibbon")
    @IgnoreResponseAdvice
    @SuppressWarnings("unchecked")
    public CommonResponse<List<AdPlan>> getAdPlanListByRibbon(@RequestBody AdPlanGetRequest request) {
        log.info("ad-search: getAdPlanListByRibbon -> {}", JSON.toJSONString(request));
        // 使用ribbon完成对微服务的调用
        return restTemplate.postForEntity(
                "http://eureka-client-ad-sponsor/ad-sponsor/get/adPlan",
                request,
                CommonResponse.class
        ).getBody();
    }

    @PostMapping("/fetchAds")
    public SearchResponse fetchAds(@RequestBody SearchRequest request) {
        log.info("ad-search: fetchAds -> {}", JSON.toJSONString(request));
        return iSearch.fetchAds(request);
    }
}
