package top.ezttf.ad.index;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import top.ezttf.ad.index.adunit.AdUnitConstants;
import top.ezttf.ad.index.adunit.AdUnitIndex;

import java.util.Set;

/**
 * @author yuwen
 * @date 2019/1/30
 */
@Slf4j
@SpringBootTest
@RunWith(SpringRunner.class)
public class IndexFileLoaderTest {

    @Autowired
    private IndexFileLoader fileLoader;

    @Autowired
    private AdUnitIndex unitIndex;

    @Test
    public void testInit() {
        fileLoader.init();
    }

    @Test
    public void testUnitMatch() {
        Set<Long> unitIds = unitIndex.match(AdUnitConstants.PositionType.KAI_PING);
        unitIds.forEach(unitId -> log.debug(unitId.toString()));
    }

}
