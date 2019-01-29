package top.ezttf.ad.service.impl;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.ezttf.ad.constant.CommonStatus;
import top.ezttf.ad.dao.*;
import top.ezttf.ad.dump.table.*;
import top.ezttf.ad.service.IDumpService;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/29
 */
@Slf4j
@Service
public class DumpServiceImpl implements IDumpService {

    private final AdPlanMapper adPlanMapper;
    private final AdUnitMapper adUnitMapper;
    private final AdCreativeMapper adCreativeMapper;
    private final CreativeUnitMapper creativeUnitMapper;
    private final AdUnitDistrictMapper adUnitDistrictMapper;
    private final AdUnitItMapper adUnitItMapper;
    private final AdUnitKeywordMapper adUnitKeywordMapper;

    @Autowired
    public DumpServiceImpl(AdPlanMapper adPlanMapper, AdUnitMapper adUnitMapper, AdCreativeMapper adCreativeMapper,
                           CreativeUnitMapper creativeUnitMapper, AdUnitDistrictMapper adUnitDistrictMapper,
                           AdUnitItMapper adUnitItMapper, AdUnitKeywordMapper adUnitKeywordMapper) {
        this.adPlanMapper = adPlanMapper;
        this.adUnitMapper = adUnitMapper;
        this.adCreativeMapper = adCreativeMapper;
        this.creativeUnitMapper = creativeUnitMapper;
        this.adUnitDistrictMapper = adUnitDistrictMapper;
        this.adUnitItMapper = adUnitItMapper;
        this.adUnitKeywordMapper = adUnitKeywordMapper;
    }


    /**
     * 导出推广计划
     * @param fileName 导出文件
     */
    public void dumpAdPlanTable(String fileName) {
        List<AdPlanTable> adPlanTableList = adPlanMapper.selectPlanTableByPlanStatus(
                CommonStatus.VALID.getStatus()
        );
        if (CollectionUtils.isEmpty(adPlanTableList)) {
            return;
        }
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdPlanTable planTable : adPlanTableList) {
                writer.write(JSON.toJSONString(planTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump planTable error");
        }
    }


    /**
     * 导出推广单元
     * @param fileName 导出文件
     */
    public void dumpAdUnitTable(String fileName) {
        List<AdUnitTable> adUnitTableList = adUnitMapper.selectUnitTableByUnitStatus(
                CommonStatus.VALID.getStatus()
        );
        if (CollectionUtils.isEmpty(adUnitTableList)) {
            return;
        }
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitTable adUnitTable : adUnitTableList) {
                writer.write(JSON.toJSONString(adUnitTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump unitTable error");
        }
    }

    /**
     * 创意数据导出
     * @param fileName 导出文件
     */
    public void dumpCreativeTable(String fileName) {
        List<AdCreativeTable> creativeTableList = adCreativeMapper.selectCreativeTable();
        if (CollectionUtils.isEmpty(creativeTableList)) {
            return;
        }
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdCreativeTable creativeTable : creativeTableList) {
                writer.write(JSON.toJSONString(creativeTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump creativeTable error");
        }
    }

    /**
     * 导出创意与推广单元关联
     * @param fileName 导出数据
     */
    public void dumpCreativeUnitTable(String fileName) {
        List<AdCreativeUnitTable> creativeUnitTableList = creativeUnitMapper.selectCreativeUnitTable();
        if (CollectionUtils.isEmpty(creativeUnitTableList)) {
            return;
        }
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdCreativeUnitTable creativeUnitTable : creativeUnitTableList) {
                writer.write(JSON.toJSONString(creativeUnitTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump creativeUnit table error");
        }
    }


    /**
     * 导出单元地域关联
     * @param fileName 导出文件
     */
    public void dumpAdUnitDistrict(String fileName) {
        List<AdUnitDistrictTable> adUnitDistrictTableList = adUnitDistrictMapper.selectAdUnitDistrictTable();
        if (CollectionUtils.isEmpty(adUnitDistrictTableList)) {
            return;
        }
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitDistrictTable adUnitDistrictTable : adUnitDistrictTableList) {
                writer.write(JSON.toJSONString(adUnitDistrictTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adUnitDistrict table error");
        }
    }


    public void dumpAdUnitItTable(String fileName) {
        List<AdUnitItTable> adUnitItTableList = adUnitItMapper.selectAdUnitItTable();
        if (CollectionUtils.isEmpty(adUnitItTableList)) {
            return;
        }
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitItTable adUnitItTable : adUnitItTableList) {
                writer.write(JSON.toJSONString(adUnitItTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adUnitIt table error");
        }
    }


    public void dumpAdUnitKeywordTable(String fileName) {
        List<AdUnitKeywordTable> adUnitKeywordTableList = adUnitKeywordMapper.selectAdUnitKeywordTable();
        if (CollectionUtils.isEmpty(adUnitKeywordTableList)) {
            return;
        }
        Path path = Paths.get(fileName);
        try (BufferedWriter writer = Files.newBufferedWriter(path)) {
            for (AdUnitKeywordTable adUnitKeywordTable : adUnitKeywordTableList) {
                writer.write(JSON.toJSONString(adUnitKeywordTable));
                writer.newLine();
            }
            writer.flush();
        } catch (IOException e) {
            log.error("dump adUnitKeyword table error");
        }
    }

}
