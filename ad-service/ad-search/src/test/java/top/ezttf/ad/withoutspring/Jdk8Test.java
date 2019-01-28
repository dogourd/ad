package top.ezttf.ad.withoutspring;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentSkipListSet;
import java.util.function.Supplier;

/**
 * @author yuwen
 * @date 2019/1/27
 */
@Slf4j
@RunWith(BlockJUnit4ClassRunner.class)
public class Jdk8Test {

    @Test
    public void testLambda() {
        Map<String, Set<Long>> map = new HashMap<>();
        Set<Long> set = getOrCreate("key", map, ConcurrentSkipListSet::new);
        set.add(666L);
        log.debug("now the map is {}", map);

        //result:
        //09:59:34.002 [main] DEBUG top.ezttf.ad.withoutspring.Jdk8Test - now the map is {key=[666]}

        set = getOrCreate("key", map, ConcurrentSkipListSet::new);
        set.add(777L);
        log.debug("now the map is {}", map);
    }

    private static<K, V> V getOrCreate(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }


}
