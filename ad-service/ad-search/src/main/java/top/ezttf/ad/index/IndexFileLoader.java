package top.ezttf.ad.index;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Component;
import top.ezttf.ad.dump.DConstant;
import top.ezttf.ad.dump.table.*;
import top.ezttf.ad.handler.AdLevelDataHandler;
import top.ezttf.ad.mysql.constant.OpType;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用于实现全量索引的加载, 读取数据表导出的文件, 加载索引
 *
 * @author yuwen
 * @date 2019/1/30
 */
@Slf4j
@Component
@DependsOn(value = {"dataTable"})     // 此处不必要, 因为DataTable注册优先级已经设置为最高, 但为表示出依赖关系顾写此注解
public class IndexFileLoader {

    /**
     * 将数据文件加载到检索系统中, 构建全量索引
     *
     * 应该在检索系统启动的时候, 就完成全量索引的加载, 即在{@link IndexFileLoader} 加载到sprig 容器中后
     * 该方法就应该被执行
     *
     * 注意! 因为索引之间存在层级的依赖关系, 所以方法调用过程中, 索引层级加载顺序不能改变
     */
    @PostConstruct
    public void init() {
        // 第二层级的 推广计划 索引加载
        List<String> adPlanStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_PLAN)
        );
        adPlanStrings.forEach(adPlanString -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(adPlanString, AdPlanTable.class),
                OpType.ADD
        ));

        // 第二层级的 创意 索引加载
        List<String> adCreativeStrings = loadDumpData(
            String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE)
        );
        adCreativeStrings.forEach(adCreativeString -> AdLevelDataHandler.handleLevel2(
                JSON.parseObject(adCreativeString, AdCreativeTable.class),
                OpType.ADD
        ));

        // 第三层级的 推广单元 索引加载
        List<String> adUnitStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT)
        );
        adUnitStrings.forEach(adUnitString -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(adUnitString, AdUnitTable.class),
                OpType.ADD
        ));

        // 第三层级的 创意与推广单元关联 索引加载
        List<String> adCreativeUnitStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_CREATIVE_UNIT)
        );
        adCreativeUnitStrings.forEach(adCreativeUnitString -> AdLevelDataHandler.handleLevel3(
                JSON.parseObject(adCreativeUnitString, AdCreativeUnitTable.class),
                OpType.ADD
        ));

        // 第四层级  推广单元地域限制 索引加载
        List<String> adUnitDistrictStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_DISTRICT)
        );
        adUnitDistrictStrings.forEach(adUnitDistrictString -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(adUnitDistrictString, AdUnitDistrictTable.class),
                OpType.ADD
        ));

        // 第四层级 推广单元兴趣限制 索引加载
        List<String> adUnitItStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_IT)
        );
        adUnitItStrings.forEach(adUnitItString -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(adUnitItString, AdUnitItTable.class),
                OpType.ADD
        ));

        // 第四层级 推广单元关键词限制 索引加载
        List<String> adUnitKeywordStrings = loadDumpData(
                String.format("%s%s", DConstant.DATA_ROOT_DIR, DConstant.AD_UNIT_KEYWORD)
        );
        adUnitKeywordStrings.forEach(adUnitKeywordString -> AdLevelDataHandler.handleLevel4(
                JSON.parseObject(adUnitKeywordString, AdUnitKeywordTable.class),
                OpType.ADD
        ));
    }


    /**
     * 给定文件全路径名, 从文件中读取每行数据。放到字符串列表返回
     * @param fileName
     * @return
     */
    private List<String> loadDumpData(String fileName) {
        Path path = Paths.get(fileName);
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            return reader.lines().collect(Collectors.toList());
        }catch (IOException e) {
            log.error("ad-search IndexFileLoader io exception");
            throw new RuntimeException(e.getMessage());
        }
    }
}
