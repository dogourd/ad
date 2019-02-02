package top.ezttf.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 维度限制
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DistrictFeature {

    private List<ProvinceAndCity> districts;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ProvinceAndCity {
        private String province;
        private String city;
    }
}
