package top.ezttf.ad.index;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author yuwen
 * @date 2019/1/30
 */
@SpringBootTest
@RunWith(SpringRunner.class)
public class IndexFileLoaderTest {

    @Autowired
    private IndexFileLoader fileLoader;

    @Test
    public void testInit() {
        fileLoader.init();
    }


}
