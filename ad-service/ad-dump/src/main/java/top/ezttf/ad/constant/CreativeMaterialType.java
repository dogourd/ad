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
public enum CreativeMaterialType {

    /**
     * 物料类型
     */
    JPG(1, "jpg"),
    BMP(2, "bmp"),

    MP4(3, "mp4"),
    AVI(4, "avi"),

    TXT(5, "txt");


    private int type;
    private String desc;
}
