package top.ezttf.ad.service;

import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.vo.*;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public interface IAdUnitService {

    AdUnitResponse createUnit(AdUnitRequest adUnitRequest) throws AdException;

    /** 创建推广单元关键词(keyword)接口*/
    AdUnitKeywordResponse createUnitKeyWord(AdUnitKeywordRequest request) throws AdException;

    AdUnitItResponse createUnitIt(AdUnitItRequest request) throws AdException;

    AdUnitDistrictResponse createUnitDistrict(AdUnitDistrictRequest request) throws AdException;

    CreativeUnitResponse createCreativeUnit(CreativeUnitRequest request) throws AdException;


}
