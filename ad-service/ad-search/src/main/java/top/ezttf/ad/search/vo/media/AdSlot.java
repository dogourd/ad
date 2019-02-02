package top.ezttf.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 广告位信息
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class AdSlot {

    /**
     * 广告位的编码
     */
    private String adSlotCode;

    /**
     * 广告位的流量类型
     */
    private Integer positionType;

    /**
     * 广告位宽高
     */
    private Integer width;
    private Integer height;

    /**
     * 广告位的物料类型, 可以支持多种物料类型    图片, 视频...
     */
    private List<Integer> type;

    /**
     * 广告位的最低出价
     */
    private Integer minCpm;
}
