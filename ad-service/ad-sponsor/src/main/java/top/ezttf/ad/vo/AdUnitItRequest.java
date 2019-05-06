package top.ezttf.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/22
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdUnitItRequest {

    private List<UnitIt> unitItList;

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UnitIt {
        private Long unitId;
        private String itTag;
    }
}
