package top.ezttf.ad.search.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Service;
import top.ezttf.ad.index.CommonStatus;
import top.ezttf.ad.index.DataTable;
import top.ezttf.ad.index.adunit.AdUnitIndex;
import top.ezttf.ad.index.adunit.AdUnitObject;
import top.ezttf.ad.index.creative.CreativeIndex;
import top.ezttf.ad.index.creative.CreativeObject;
import top.ezttf.ad.index.creativeunit.CreativeUnitIndex;
import top.ezttf.ad.index.district.UnitDistrictIndex;
import top.ezttf.ad.index.interest.UnitItIndex;
import top.ezttf.ad.index.keyword.UnitKeywordIndex;
import top.ezttf.ad.search.ISearch;
import top.ezttf.ad.search.vo.SearchRequest;
import top.ezttf.ad.search.vo.SearchResponse;
import top.ezttf.ad.search.vo.feature.DistrictFeature;
import top.ezttf.ad.search.vo.feature.FeatureRelation;
import top.ezttf.ad.search.vo.feature.ItFeature;
import top.ezttf.ad.search.vo.feature.KeywordFeature;
import top.ezttf.ad.search.vo.media.AdSlot;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author yuwen
 * @date 2019/2/2
 */
@Slf4j
@Service
public class SearchImpl implements ISearch {

    public SearchResponse fallback(SearchRequest request, Throwable throwable) {
        return null;
    }


    @Override
    @HystrixCommand(fallbackMethod = "fallback")
    public SearchResponse fetchAds(SearchRequest request) {
        // 请求的广告位信息
        List<AdSlot> adSlots = request.getRequestInfo().getAdSlots();
        // 三个 广告特征
        KeywordFeature keywordFeature = request.getFeatureInfo().getKeywordFeature();
        DistrictFeature districtFeature = request.getFeatureInfo().getDistrictFeature();
        ItFeature itFeature = request.getFeatureInfo().getItFeature();
        // 特征之间的结合关系
        FeatureRelation featureRelation = request.getFeatureInfo().getFeatureRelation();

        SearchResponse response = new SearchResponse();
        Map<String, List<SearchResponse.Creative>> adSlot2Ads = response.getAdSlot2Ads();
        adSlots.forEach(adSlot -> {
            Set<Long> targetUnitIds;
            Set<Long> adUnitIdSet = DataTable.of(AdUnitIndex.class).match(adSlot.getPositionType());
            if (featureRelation == FeatureRelation.AND) {
                filterKeywordFeature(adUnitIdSet, keywordFeature);
                filterDistrictFeature(adUnitIdSet, districtFeature);
                filterItFeature(adUnitIdSet, itFeature);

                targetUnitIds = adUnitIdSet;
            } else {
                targetUnitIds = getOrRelationUnitIds(adUnitIdSet, keywordFeature, districtFeature, itFeature);
            }
            // 通过推广单元id 获取到推广单元
            List<AdUnitObject> adUnitObjects = DataTable.of(AdUnitIndex.class).fetch(targetUnitIds);
            // 通过推广单元状态进行推广单元过滤
            filterAdUnitAndPlanStatus(adUnitObjects, CommonStatus.VALID);
            // 通过推广单元获取到创意对象id
            List<Long> creativeIds = DataTable.of(CreativeUnitIndex.class).selectAds(adUnitObjects);
            // 通过创意对象id 获取创意对象
            List<CreativeObject> creativeObjects = DataTable.of(CreativeIndex.class).fetch(creativeIds);

            // 通过adSlot实现 对创意对象进行过滤
            filterCreativeByAdSlot(creativeObjects, adSlot.getHeight(), adSlot.getWidth(), adSlot.getType());
            adSlot2Ads.put(adSlot.getAdSlotCode(), buildCreativeResponse(creativeObjects));
        });
        log.info("fetch ads : {}-{}", JSON.toJSONString(request), JSON.toJSONString(response));
        return response;
    }


    private List<SearchResponse.Creative> buildCreativeResponse(List<CreativeObject> objects) {
        if (CollectionUtils.isEmpty(objects)) {
            return Collections.emptyList();
        }
        // TODO 此处只是为了方便调试, 只返回一个可以扩展返回list
        // 随机地返回一个创意对象
        CreativeObject object = objects.get(
            Math.abs(ThreadLocalRandom.current().nextInt()) % objects.size()
        );
        return Collections.singletonList(SearchResponse.convert(object));
    }
    /**
     * 根据广告位信息过滤
     * @param objects
     * @param height
     * @param width
     * @param types
     */
    private void filterCreativeByAdSlot(List<CreativeObject> objects, Integer height,
                                        Integer width, List<Integer> types) {
        if (CollectionUtils.isEmpty(objects)) {
            return;
        }
        CollectionUtils.filter(objects, createObject ->
                createObject.getAuditStatus().equals(CommonStatus.VALID.getStatus()) &&
                        createObject.getWidth().equals(width) &&
                        createObject.getHeight().equals(height) &&
                        types.contains(createObject.getType())
        );
    }

    /**
     * 推广单元状态过滤
     *
     * @return
     */
    private void filterAdUnitAndPlanStatus(List<AdUnitObject> unitObjects, CommonStatus commonStatus) {
        if (CollectionUtils.isEmpty(unitObjects)) {
            return;
        }
        CollectionUtils.filter(unitObjects, unitObject ->
                unitObject.getUnitStatus().equals(commonStatus.getStatus()) &&
                        unitObject.getAdPlanObject().getPlanStatus() == commonStatus.getStatus()
        );
    }

    /**
     * and 类型过滤
     *
     * @param adUnitIds
     * @param feature
     */
    private void filterKeywordFeature(Collection<Long> adUnitIds, KeywordFeature feature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(feature.getKeywords())) {
            CollectionUtils.filter(adUnitIds, adUnitId ->
                    DataTable.of(UnitKeywordIndex.class).match(adUnitId, feature.getKeywords())
            );
        }
    }

    private void filterDistrictFeature(Collection<Long> adUnitIds, DistrictFeature feature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(feature.getDistricts())) {
            CollectionUtils.filter(adUnitIds, adUnitId ->
                    DataTable.of(UnitDistrictIndex.class).match(adUnitId, feature.getDistricts())
            );
        }
    }

    private void filterItFeature(Collection<Long> adUnitIds, ItFeature feature) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return;
        }
        if (CollectionUtils.isNotEmpty(feature.getIts())) {
            CollectionUtils.filter(adUnitIds, adUnitId ->
                    DataTable.of(UnitItIndex.class).match(adUnitId, feature.getIts())
            );
        }
    }


    /**
     * or 类型过滤
     */
    private Set<Long> getOrRelationUnitIds(
            Set<Long> adUnitIds,
            KeywordFeature keywordFeature,
            DistrictFeature districtFeature,
            ItFeature itFeature
    ) {
        if (CollectionUtils.isEmpty(adUnitIds)) {
            return Collections.emptySet();
        }
        Set<Long> keywordUnitIdS = Sets.newHashSet(adUnitIds);
        Set<Long> districtUnitIds = Sets.newHashSet(adUnitIds);
        Set<Long> itUnitIds = Sets.newHashSet(adUnitIds);

        filterKeywordFeature(keywordUnitIdS, keywordFeature);
        filterDistrictFeature(districtUnitIds, districtFeature);
        filterItFeature(itUnitIds, itFeature);

        return Sets.newHashSet(
                CollectionUtils.union(
                        CollectionUtils.union(keywordUnitIdS, districtUnitIds),
                        itUnitIds
                )
        );
    }


}