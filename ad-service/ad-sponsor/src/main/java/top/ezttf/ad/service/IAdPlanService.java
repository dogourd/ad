package top.ezttf.ad.service;

import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.pojo.AdPlan;
import top.ezttf.ad.vo.AdPlanGetRequest;
import top.ezttf.ad.vo.AdPlanRequest;
import top.ezttf.ad.vo.AdPlanResponse;

import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public interface IAdPlanService {

    /**
     * <h2>创建推广计划<h2/>
     */
    AdPlanResponse createAdPlan(AdPlanRequest adPlanRequest) throws AdException;

    /**
     * <h2>获取推广计划</h2>
     */
    List<AdPlan> getAdPlanByIdList(AdPlanGetRequest adPlanGetRequest) throws AdException;

    /**
     * <h2> 更新推广计划 <h2/>
     */
    AdPlanResponse updateAdPlan(AdPlanRequest adPlanRequest) throws AdException;

    /**
     * <h2>删除推广计划</h2>
     */
    void deleteAdPlan(AdPlanRequest adPlanRequest) throws AdException;
}
