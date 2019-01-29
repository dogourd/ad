package top.ezttf.ad.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ezttf.ad.dao.*;
import top.ezttf.ad.pojo.AdUnitDistrict;
import top.ezttf.ad.service.IDumpService;

/**
 * @author yuwen
 * @date 2019/1/29
 */
@Service
public class DumpServiceImpl implements IDumpService {

    private final AdPlanMapper adPlanMapper;
    private final AdUnitMapper adUnitMapper;
    private final AdCreativeMapper adCreativeMapper;
    private final CreativeUnitMapper creativeUnitMapper;
    private final AdUnitDistrict adUnitDistrict;
    private final AdUnitItMapper adUnitItMapper;
    private final AdUnitKeywordMapper adUnitKeywordMapper;

    @Autowired
    public DumpServiceImpl(AdPlanMapper adPlanMapper, AdUnitMapper adUnitMapper, AdCreativeMapper adCreativeMapper, CreativeUnitMapper creativeUnitMapper, AdUnitDistrict adUnitDistrict, AdUnitItMapper adUnitItMapper, AdUnitKeywordMapper adUnitKeywordMapper) {
        this.adPlanMapper = adPlanMapper;
        this.adUnitMapper = adUnitMapper;
        this.adCreativeMapper = adCreativeMapper;
        this.creativeUnitMapper = creativeUnitMapper;
        this.adUnitDistrict = adUnitDistrict;
        this.adUnitItMapper = adUnitItMapper;
        this.adUnitKeywordMapper = adUnitKeywordMapper;
    }
}
