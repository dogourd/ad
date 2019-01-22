package top.ezttf.ad.constant;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * @author yuwen
 * @date 2019/1/20
 */
@Getter
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public enum CreativeType {

    /**
     * 媒体类型
     */
    IMAGE(1, "图片"),
    VIDEO(2, "视频"),
    TEXT(3, "文本");

    private int type;
    private String desc;
}
