package top.ezttf.ad.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import top.ezttf.ad.constant.Constants;
import top.ezttf.ad.dao.AdPlanRepository;
import top.ezttf.ad.dao.AdUnitRepository;
import top.ezttf.ad.dao.CreativeRepository;
import top.ezttf.ad.dao.unitcondition.AdUnitDistrictRepository;
import top.ezttf.ad.dao.unitcondition.AdUnitItRepository;
import top.ezttf.ad.dao.unitcondition.AdUnitKeywordRepository;
import top.ezttf.ad.dao.unitcondition.CreativeUnitRepository;
import top.ezttf.ad.exception.*;
import top.ezttf.ad.pojo.AdPlan;
import top.ezttf.ad.pojo.AdUnit;
import top.ezttf.ad.pojo.unitcondition.AdUnitDistrict;
import top.ezttf.ad.pojo.unitcondition.AdUnitIt;
import top.ezttf.ad.pojo.unitcondition.AdUnitKeyword;
import top.ezttf.ad.pojo.unitcondition.CreativeUnit;
import top.ezttf.ad.service.IAdUnitService;
import top.ezttf.ad.vo.*;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author yuwen
 * @date 2019/1/20
 */
@Slf4j
@Service
public class AdUnitServiceImpl implements IAdUnitService {

    private final AdPlanRepository adPlanRepository;

    private final AdUnitRepository adUnitRepository;

    private final AdUnitKeywordRepository adUnitKeywordRepository;

    private final AdUnitItRepository adUnitItRepository;

    private final AdUnitDistrictRepository adUnitDistrictRepository;

    private final CreativeRepository creativeRepository;

    private final CreativeUnitRepository creativeUnitRepository;

    @Autowired
    public AdUnitServiceImpl(AdPlanRepository adPlanRepository,
                             AdUnitRepository adUnitRepository,
                             AdUnitDistrictRepository adUnitDistrictRepository,
                             AdUnitKeywordRepository adUnitKeywordRepository,
                             AdUnitItRepository adUnitItRepository,
                             CreativeRepository creativeRepository,
                             CreativeUnitRepository creativeUnitRepository) {
        this.adPlanRepository = adPlanRepository;
        this.adUnitRepository = adUnitRepository;
        this.adUnitDistrictRepository = adUnitDistrictRepository;
        this.adUnitKeywordRepository = adUnitKeywordRepository;
        this.adUnitItRepository = adUnitItRepository;
        this.creativeRepository = creativeRepository;
        this.creativeUnitRepository = creativeUnitRepository;
    }

    @Override
    public AdUnitResponse createUnit(AdUnitRequest adUnitRequest) throws AdException {
        if (!adUnitRequest.createValidate()) {
            throw new AdUnitException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        Optional<AdPlan> adPlan = adPlanRepository.findById(adUnitRequest.getPlanId());
        if (!adPlan.isPresent()) {
            throw new AdPlanException(Constants.ErrorMsg.CANNOT_FOUND_RECORD);
        }
        AdUnit adUnit = adUnitRepository.findByPlanIdAndUnitName(adUnitRequest.getPlanId(), adUnitRequest.getUnitName());
        if (adUnit != null) {
            throw new AdUnitException(Constants.ErrorMsg.SAME_NAME_UNIT_ERROR);
        }
        AdUnit unit = adUnitRepository.save(
                new AdUnit(
                        adUnitRequest.getPlanId(),
                        adUnitRequest.getUnitName(),
                        adUnitRequest.getPositionType(),
                        adUnitRequest.getBudget()
                )
        );
        return new AdUnitResponse(unit.getId(), unit.getUnitName());
    }

    @Override
    public AdUnitKeywordResponse createUnitKeyWord(AdUnitKeywordRequest request) throws AdException {
        List<Long> unitIdList = request.getUnitKeywordList()
                .stream()
                .map(AdUnitKeywordRequest.UnitKeyword::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIdList)) {
            throw new AdUnitKeywordException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Collections.emptyList();
        List<AdUnitKeyword> unitKeywordList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitKeywordList())) {
            request.getUnitKeywordList().forEach(unitKeyword -> unitKeywordList.add(
                    new AdUnitKeyword(unitKeyword.getUnitId(), unitKeyword.getKeyword())
            ));
            idList = adUnitKeywordRepository.saveAll(unitKeywordList).stream()
                    .map(AdUnitKeyword::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitKeywordResponse(idList);
    }

    @Override
    public AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException {
        List<Long> unitIdList = request.getUnitItList().stream()
                .map(AdUnitItRequest.UnitIt::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIdList)) {
            throw new AdUnitItException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Collections.emptyList();
        List<AdUnitIt> unitItList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitItList())) {
            request.getUnitItList().forEach(unitIt -> unitItList.add(
                    new AdUnitIt(unitIt.getUnitId(), unitIt.getItTag())
            ));
            idList = adUnitItRepository.saveAll(unitItList).stream()
                    .map(AdUnitIt::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitItResponse(idList);
    }

    @Override
    public AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException {
        List<Long> unitIdList = request.getUnitDistrictList().stream()
                .map(AdUnitDistrictRequest.UnitDistrict::getUnitId)
                .collect(Collectors.toList());
        if (!isRelatedUnitExist(unitIdList)) {
            throw new AdUnitDistrictException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Collections.emptyList();
        List<AdUnitDistrict> unitDistrictList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getUnitDistrictList())) {
            request.getUnitDistrictList().forEach(unitDistrict -> unitDistrictList.add(
                    new AdUnitDistrict(
                            unitDistrict.getUnitId(),
                            unitDistrict.getProvince(),
                            unitDistrict.getCity())
            ));
            idList = adUnitDistrictRepository.saveAll(unitDistrictList).stream()
                    .map(AdUnitDistrict::getId)
                    .collect(Collectors.toList());
        }
        return new AdUnitDistrictResponse(idList);
    }

    @Override
    public CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException {
        List<Long> unitIdList = request.getCreativeUnitItemList().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getUnitId)
                .collect(Collectors.toList());
        List<Long> creativeIdList = request.getCreativeUnitItemList().stream()
                .map(CreativeUnitRequest.CreativeUnitItem::getCreativeId)
                .collect(Collectors.toList());
        if (!(isRelatedUnitExist(unitIdList) && isRelatedCreativeExist(creativeIdList))) {
            throw new CreativeUnitException(Constants.ErrorMsg.REQUEST_PARAM_ERROR);
        }
        List<Long> idList = Collections.emptyList();
        List<CreativeUnit> creativeUnitList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(request.getCreativeUnitItemList())) {
            request.getCreativeUnitItemList().forEach(creativeUnitItem -> creativeUnitList.add(
                    new CreativeUnit(creativeUnitItem.getCreativeId(), creativeUnitItem.getUnitId())
            ));
            idList = creativeUnitRepository.saveAll(creativeUnitList).stream()
                    .map(CreativeUnit::getId)
                    .collect(Collectors.toList());
        }
        return new CreativeUnitResponse(idList);
    }


    private boolean isRelatedUnitExist(List<Long> idList) {
        if (CollectionUtils.isEmpty(idList)) {
            return false;
        }
        return adUnitRepository.findAllById(idList).size() == new HashSet<>(idList).size();
    }

    private boolean isRelatedCreativeExist(List<Long> creativeIdList) {
        if (CollectionUtils.isEmpty(creativeIdList)) {
            return false;
        }
        return creativeRepository.findAllById(creativeIdList).size() == new HashSet<>(creativeIdList).size();
    }
}
