package top.ezttf.ad.util;

import org.apache.commons.codec.digest.DigestUtils;
import top.ezttf.ad.exception.AdException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.Locale;

/**
 * @author yuwen
 * @date 2019/1/20
 */
public class CommonUtils {

    private static final String[] LOCAL_DATE_PARSE_PATTERN = {
            "yyyy-MM-dd", "yyyy/MM/dd", "yyyy.MM.dd"
    };
    private static final String[] LOCAL_DATE_TIME_PARSE_PATTERN = {
            "yyyy-MM-dd HH:mm:ss", "yyyy/MM/dd HH:mm:ss", "yyyy.MM.dd HH:mm:ss"
    };
    private static DateTimeFormatter formatter;


    public static String sha512(String originString) {
        return Arrays.toString(DigestUtils.sha512(originString)).toUpperCase();
    }

    public static String md5(String originString) {
        return DigestUtils.md5Hex(originString).toUpperCase();
    }

    public static LocalDate parseStringLocalDate(String dateString) throws AdException {
        for (String s : LOCAL_DATE_PARSE_PATTERN) {
            formatter = DateTimeFormatter.ofPattern(s, Locale.getDefault());
            try {
                return LocalDate.parse(dateString, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new AdException("unable parse String to LocalDate");
    }

    public static LocalDateTime parseStringLocalDateTime(String dateTimeString) throws AdException {
        for (String s : LOCAL_DATE_TIME_PARSE_PATTERN) {
            formatter = DateTimeFormatter.ofPattern(s, Locale.getDefault());
            try {
                return LocalDateTime.parse(dateTimeString, formatter);
            } catch (DateTimeParseException ignored) {
            }
        }
        throw new AdException("unable parse String to LocalDate");
    }

}
