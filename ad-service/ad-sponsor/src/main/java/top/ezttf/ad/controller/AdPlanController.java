package top.ezttf.ad.controller;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.pojo.AdPlan;
import top.ezttf.ad.service.IAdPlanService;
import top.ezttf.ad.vo.AdPlanGetRequest;
import top.ezttf.ad.vo.AdPlanRequest;
import top.ezttf.ad.vo.AdPlanResponse;

import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/22
 */
@Slf4j
@RestController
public class AdPlanController {

    private final IAdPlanService iAdPlanService;

    @Autowired
    public AdPlanController(IAdPlanService iAdPlanService) {
        this.iAdPlanService = iAdPlanService;
    }

    @PostMapping("/create/adPlan")
    public AdPlanResponse createAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: createAdPlan -> {}", JSON.toJSONString(request));
        return iAdPlanService.createAdPlan(request);
    }

    @PostMapping("/get/adPlan")
    public List<AdPlan> getAdPlanByIdList(@RequestBody AdPlanGetRequest request) throws AdException {
        log.info("ad-sponsor: getAdPlanByIdList -> {}", JSON.toJSONString(request));
        return iAdPlanService.getAdPlanByIdList(request);
    }

    @PutMapping("/update/adPlan")
    public AdPlanResponse updateAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: updateAdPlan -> {}", JSON.toJSONString(request));
        return iAdPlanService.updateAdPlan(request);
    }

    @DeleteMapping("/delete/adPlan")
    public void deleteAdPlan(@RequestBody AdPlanRequest request) throws AdException {
        log.info("ad-sponsor: deleteAdPlan -> {}", JSON.toJSONString(request));
        iAdPlanService.deleteAdPlan(request);
    }
}
