package top.ezttf.ad.withoutspring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

/**
 * @author yuwen
 * @date 2019/1/30
 */
@Slf4j
@RunWith(BlockJUnit4ClassRunner.class)
public class StringFormatTest {

    @Test
    public void testStringFormat() {
        log.debug(String.format("%s%s", "first", "second"));
    }
}
