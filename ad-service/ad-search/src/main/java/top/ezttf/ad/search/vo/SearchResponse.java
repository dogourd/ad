package top.ezttf.ad.search.vo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.ezttf.ad.index.creative.CreativeObject;

import java.util.List;
import java.util.Map;

/**
 * 检索服务, 媒体响应对象
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchResponse {

    /**
     * key: {@link top.ezttf.ad.search.vo.media.AdSlot.slotCode}
     * value:　多个创意信息
     */
    private Map<String, List<Creative>> adSlot2Ads = Maps.newHashMap();


    /**
     * 广告创意
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Creative {

        private Long adId;
        /**
         * 广告Url, 媒体方通过此url下载广告数据
         */
        private String adUrl;
        private Integer width;
        private Integer height;
        private Integer type;
        private Integer materialType;

        /**
         * 展示监测 url
         * 当广告返回给媒体方, 媒体方会对广告数据进行曝光(即对广告进行展示)
         */
        private List<String> showMonitorUrl = Lists.newArrayList("www.baidu.com", "www.google.com");

        /**
         * 点击监测 url
         * 用户对广告数据进行了点击
         */
        private List<String> clickMonitorUrl = Lists.newArrayList("www.baidu.com", "www.google.com");


    }
    public static Creative convert(CreativeObject creativeObject) {
        Creative creative = new Creative();
        creative.setAdId(creativeObject.getAdId());
        creative.setAdUrl(creativeObject.getAdUrl());
        creative.setHeight(creativeObject.getHeight());
        creative.setWidth(creativeObject.getWidth());
        creative.setType(creativeObject.getType());
        creative.setMaterialType(creativeObject.getMaterialType());

        return creative;
    }
}
