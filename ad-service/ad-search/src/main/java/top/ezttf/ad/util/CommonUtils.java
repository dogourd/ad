package top.ezttf.ad.util;

import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;

/**
 * @author yuwen
 * @date 2019/1/26
 */
@Slf4j
public class CommonUtils {


    /**
     * 若 {@param map} 中不存在 {@param key} 这个键,
     * 则使用传进来的 {@param factory} 返回一个新的对象 {@code factory.get()}
     *
     * @return
     */
    public static <K, V> V getOrCreateMap(K key, Map<K, V> map, Supplier<V> factory) {
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


    public static LocalDateTime parseStringLocalDateTime(String dateTimeString) {
        try {
            return LocalDateTime
                    .parse(
                            dateTimeString,
                            DateTimeFormatter.ofPattern("EEE MMM dd HH:mm:ss zzz yyyy", Locale.US)
                    )
                    .minusHours(8);
        } catch (DateTimeParseException e) {
            log.error("parseStringLocalDateTime error: {}", e);
            return null;
        }
    }

    public static void main(String[] args) {
        log.debug("{}", parseStringLocalDateTime("Tue Jan 01 08:00:00 CST 2019"));
    }
}
