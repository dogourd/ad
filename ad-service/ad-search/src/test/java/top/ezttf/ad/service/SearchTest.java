package top.ezttf.ad.service;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.ezttf.ad.search.ISearch;
import top.ezttf.ad.search.vo.SearchRequest;
import top.ezttf.ad.search.vo.feature.DistrictFeature;
import top.ezttf.ad.search.vo.feature.FeatureRelation;
import top.ezttf.ad.search.vo.feature.ItFeature;
import top.ezttf.ad.search.vo.feature.KeywordFeature;
import top.ezttf.ad.search.vo.media.AdSlot;
import top.ezttf.ad.search.vo.media.App;
import top.ezttf.ad.search.vo.media.Device;
import top.ezttf.ad.search.vo.media.Geo;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author yuwen
 * @date 2019/2/13
 */
@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class SearchTest {

    @Autowired
    private ISearch iSearch;

    @Test
    public void testFetchAds() {
        SearchRequest request = new SearchRequest();
        request.setMediaId("ad");

        // 第一个测试条件
        request.setRequestInfo(new SearchRequest.RequestInfo(
                "requestId",
                Collections.singletonList(new AdSlot(
                        "ad-slot-code",
                        1,
                        1080,
                        720,
                        Arrays.asList(1, 2),
                        1000
                )),
                buildExampleApp(),
                buildExampleGeo(),
                buildExampleDevice()
        ));
        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马", "大众"),
                Collections.singletonList(
                        new DistrictFeature.ProvinceAndCity("安徽省", "合肥市")
                ),
                Arrays.asList("台球", "游泳"),
                FeatureRelation.OR
        ));
        log.debug(JSON.toJSONString(request));
        log.info(JSON.toJSONString(iSearch.fetchAds(request)));



        // 第二个测试条件
        request.setRequestInfo(new SearchRequest.RequestInfo(
                "requestId",
                Collections.singletonList(new AdSlot(
                        "ad-slot-code",
                        1,
                        1080,
                        720,
                        Arrays.asList(1, 2),
                        1000
                )),
                buildExampleApp(),
                buildExampleGeo(),
                buildExampleDevice()
        ));
        request.setFeatureInfo(buildExampleFeatureInfo(
                Arrays.asList("宝马", "大众", "标致"),
                Collections.singletonList(
                        new DistrictFeature.ProvinceAndCity("安徽省", "合肥市")
                ),
                Arrays.asList("台球", "游泳"),
                FeatureRelation.AND
        ));
        log.debug(JSON.toJSONString(request));
        log.info(JSON.toJSONString(iSearch.fetchAds(request)));
    }





    private App buildExampleApp() {
        return new App("appCode", "appName",
                "packageName", "activityName");
    }

    private Geo buildExampleGeo() {
        return new Geo(100.1F, 88.1F, "北京市", "北京市");
    }

    private Device buildExampleDevice() {
        return new Device("deviceCode", "mac", "127.0.0.1", "model",
                "144 144", "720 720", "serialName"
        );
    }

    private SearchRequest.FeatureInfo buildExampleFeatureInfo(
            List<String> keywords,
            List<DistrictFeature.ProvinceAndCity> provinceAndCities,
            List<String> its,
            FeatureRelation relation
    ) {
        return new SearchRequest.FeatureInfo(
                new KeywordFeature(keywords),
                new ItFeature(its),
                new DistrictFeature(provinceAndCities),
                relation
        );
    }


}
