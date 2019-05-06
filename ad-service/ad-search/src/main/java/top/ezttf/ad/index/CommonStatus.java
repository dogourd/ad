package top.ezttf.ad.index;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuwen
 * @date 2019/2/2
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public enum CommonStatus {

    /**
     * 状态
     */
    VALID(1, "有效"),
    INVALID(0, "无效");

    private Integer status;
    private String desc;
}
