package top.ezttf.ad.withoutspring;

import com.google.common.collect.Maps;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.*;

/**
 * @author yuwen
 * @date 2019/1/31
 */
@Slf4j
@RunWith(BlockJUnit4ClassRunner.class)
public class MapTest {

    @Test
    public void testCopy() {
        List<Map<String, String>> list = new ArrayList<>();
        Map<String, String> map = new HashMap<>();
        map.put("1", "1");
        map.put("2", "2");
        list.add(map);
        Map<String, String> newMap = new HashMap<>(map);
        log.debug("newMap is {}", newMap);
        log.debug("list is {}", list);
        map.put("1", "2");
        map.put("2", "3");
        log.debug("newMap is {}", newMap);
        log.debug("list is {}", list);
    }

    @Test
    public void test() {
        Map<Integer, Integer> map = Maps.newHashMap();
        for (int i = 0; i < 5; i++) {
            map.put(i, i);
        }
        List<Integer> list = new ArrayList<>(new HashSet<>(map.keySet()));
        for (int i = 0; i < 5; i++) {
            list.remove(list.get(list.size() - 1));
        }
    }
}
