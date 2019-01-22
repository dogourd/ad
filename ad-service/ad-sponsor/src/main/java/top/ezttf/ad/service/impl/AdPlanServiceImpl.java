package top.ezttf.ad.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.ezttf.ad.constant.CommonStatus;
import top.ezttf.ad.constant.Constants;
import top.ezttf.ad.dao.AdPlanRepository;
import top.ezttf.ad.dao.AdUserRepository;
import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.exception.AdPlanException;
import top.ezttf.ad.exception.AdPlanGetException;
import top.ezttf.ad.pojo.AdPlan;
import top.ezttf.ad.pojo.AdUser;
import top.ezttf.ad.service.IAdPlanService;
import top.ezttf.ad.util.CommonUtils;
import top.ezttf.ad.vo.AdPlanGetRequest;
import top.ezttf.ad.vo.AdPlanRequest;
import top.ezttf.ad.vo.AdPlanResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * @author yuwen
 * @date 2019/1/20
 */
@Slf4j
@Service
public class AdPlanServiceImpl implements IAdPlanService {

    private final AdUserRepository adUserRepository;

    private final AdPlanRepository adPlanRepository;

    @Autowired
    public AdPlanServiceImpl(AdUserRepository adUserRepository, AdPlanRepository adPlanRepository) {
        this.adUserRepository = adUserRepository;
        this.adPlanRepository = adPlanRepository;
    }

    /**
     * <h2> 创建推广计划 </h2>
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public AdPlanResponse createAdPlan(AdPlanRequest adPlanRequest) throws AdException {
        if (!adPlanRequest.createValidate()) {
            throw new AdPlanException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        // 确保存在关联user
        Optional<AdUser> adUser = adUserRepository.findById(adPlanRequest.getUserId());
        if (!adUser.isPresent()) {
            throw new AdPlanException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }

        AdPlan oldPlan = adPlanRepository.findByUserIdAndPlanName(adPlanRequest.getUserId(), adPlanRequest.getPlanName());
        if (oldPlan != null) {
            throw new AdPlanException(Constants.ErrorMsg.SAME_NAME_PLAN_ERROR);
        }
        AdPlan newAdPlan = adPlanRepository.save(
                new AdPlan(
                        adPlanRequest.getUserId(),
                        adPlanRequest.getPlanName(),
                        CommonUtils.parseStringLocalDate(adPlanRequest.getStartDate()),
                        CommonUtils.parseStringLocalDate(adPlanRequest.getEndDate())
                )
        );
        return new AdPlanResponse(newAdPlan.getId(), newAdPlan.getPlanName());
    }

    /**
     * <h2> 获取推广计划列表 </h2>
     */
    @Override
    public List<AdPlan> getAdPlanByIdList(AdPlanGetRequest adPlanGetRequest) throws AdException{
        if (!adPlanGetRequest.validate()) {
            throw new AdPlanGetException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        return adPlanRepository.findAllByIdInAndUserId(adPlanGetRequest.getIdList(), adPlanGetRequest.getUserId());
    }

    /**
     * <h2> 更新推广计划 </h2>
     */
    @Override
    @Transactional(rollbackFor = {Exception.class})
    public AdPlanResponse updateAdPlan(AdPlanRequest adPlanRequest) throws AdException {
        if (!adPlanRequest.updateValidate()) {
            throw new AdPlanException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan oldPlan = adPlanRepository.findByIdAndUserId(adPlanRequest.getId(), adPlanRequest.getUserId());
        if (oldPlan == null) {
            throw new AdPlanException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        if (StringUtils.isNotBlank(adPlanRequest.getPlanName())) {
            oldPlan.setPlanName(adPlanRequest.getPlanName());
        }
        if (adPlanRequest.getStartDate() != null) {
            oldPlan.setStartDate(CommonUtils.parseStringLocalDate(adPlanRequest.getStartDate()));
        }
        if (adPlanRequest.getEndDate() != null) {
            oldPlan.setEndDate(CommonUtils.parseStringLocalDate(adPlanRequest.getEndDate()));
        }
        oldPlan.setUpdateTime(LocalDateTime.now());
        oldPlan = adPlanRepository.save(oldPlan);
        return new AdPlanResponse(oldPlan.getId(), oldPlan.getPlanName());
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public void deleteAdPlan(AdPlanRequest adPlanRequest) throws AdException {
        if (!adPlanRequest.deleteValidate()) {
            throw new AdPlanException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        AdPlan oldPlan = adPlanRepository.findByIdAndUserId(adPlanRequest.getId(), adPlanRequest.getUserId());
        if (oldPlan == null) {
            throw new AdPlanException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        oldPlan.setUpdateTime(LocalDateTime.now());
        oldPlan.setPlanStatus(CommonStatus.IN_VALID.getStatus());
        adPlanRepository.save(oldPlan);
    }
}
