package top.ezttf.ad.search.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.ezttf.ad.search.vo.feature.DistrictFeature;
import top.ezttf.ad.search.vo.feature.FeatureRelation;
import top.ezttf.ad.search.vo.feature.ItFeature;
import top.ezttf.ad.search.vo.feature.KeywordFeature;
import top.ezttf.ad.search.vo.media.AdSlot;
import top.ezttf.ad.search.vo.media.App;
import top.ezttf.ad.search.vo.media.Device;
import top.ezttf.ad.search.vo.media.Geo;

import java.util.List;

/**
 * 检索服务 媒体请求对象
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {

    /**
     * 媒体方请求标识
     */
    private String mediaId;

    /**
     * 请求基本信息
     */
    private RequestInfo requestInfo;

    /**
     * 请求匹配信息
     */
    private FeatureInfo featureInfo;











    /**
     * 一次请求 携带的基本信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RequestInfo {
        /**
         * 请求的唯一 id
         */
        private String requestId;

        /**
         * 广告位, 可以一次请求多个广告位
         */
        private List<AdSlot> adSlots;

        /**
         * app 信息
         */
        private App app;

        /**
         * 地理位置信息
         */
        private Geo geo;

        /**
         * 设备信息
         */
        private Device device;
    }

    /**
     * 一次请求 携带的匹配信息
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FeatureInfo {
        private KeywordFeature keywordFeature;
        private ItFeature itFeature;
        private DistrictFeature districtFeature;

        /**
         * 默认为 AND
         */
        private FeatureRelation featureRelation = FeatureRelation.AND;
    }
}
