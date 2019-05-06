package top.ezttf.ad.service;

/**
 * @author yuwen
 * @date 2019/1/29
 */
public interface IDumpService {

    void dumpAdPlanTable(String fileName);

    void dumpAdUnitTable(String fileName);

    void dumpCreativeTable(String fileName);

    void dumpCreativeUnitTable(String fileName);

    void dumpAdUnitDistrict(String fileName);

    void dumpAdUnitItTable(String fileName);

    void dumpAdUnitKeywordTable(String fileName);
}
