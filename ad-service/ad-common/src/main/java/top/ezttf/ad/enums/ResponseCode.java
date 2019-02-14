package top.ezttf.ad.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuwen
 * @date 2019/1/19
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum ResponseCode {
    /**
     * 正常相应处理
     */
    SUCCESS(0, "SUCCESS"),
    /**
     * 系统内部异常
     */
    ERROR(-1, "ERROR"),
    /**
     * 请求需要登录状态
     */
    NEED_LOGIN(10, "NEED_LOGIN"),
    /**
     * 请求参数不合法
     */
    ILLEGAL_ARGUMENT(1, "ILLEGAL_ARGUMENT");

    private int code;
    private String message;
}
