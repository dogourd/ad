package top.ezttf.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 广告位的地理位置信息
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Geo {

    /**
     * 纬度
     */
    private Float latitude;

    /**
     * 经度
     */
    private Float longitude;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;
}
