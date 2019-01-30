package top.ezttf.ad.util;

import java.util.Map;
import java.util.function.Supplier;

/**
 * @author yuwen
 * @date 2019/1/26
 */
public class CommonUtils {


    /**
     * 若 {@param map} 中不存在 {@param key} 这个键,
     * 则使用传进来的 {@param factory} 返回一个新的对象 {@code factory.get()}
     *
     * @return
     */
    public static<K, V> V getOrCreateMap(K key, Map<K, V> map, Supplier<V> factory) {
        return map.computeIfAbsent(key, k -> factory.get());
    }


    public static String stringContact(String... args) {
        StringBuilder builder = new StringBuilder();
        for (String arg : args) {
            builder.append(arg);
            builder.append("-");
        }
        return builder.deleteCharAt(builder.lastIndexOf("-")).toString();
    }

}
