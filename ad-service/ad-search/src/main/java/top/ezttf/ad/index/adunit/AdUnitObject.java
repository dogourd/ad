package top.ezttf.ad.index.adunit;

import lombok.*;
import top.ezttf.ad.index.adplan.AdPlanObject;

/**
 * 推广单元 索引对象
 *
 * @author yuwen
 * @date 2019/1/26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class AdUnitObject {

    private Long unitId;
    private Integer unitStatus;
    private Integer positionType;
    private Long planId;
    private AdPlanObject adPlanObject;

    public AdUnitObject update(AdUnitObject object) {
        if (object == null) {
            return this;
        }
        if (object.getUnitId() != null) {
            this.unitId = object.getUnitId();
        }
        if (object.getUnitStatus() != null) {
            this.unitStatus = object.getUnitStatus();
        }
        if (object.getPositionType() != null) {
            this.positionType = object.getPositionType();
        }
        if (object.getPlanId() != null) {
            this.planId = object.getPlanId();
        }
        if (object.getAdPlanObject() != null) {
            this.adPlanObject = object.getAdPlanObject();
        }
        return this;
    }
}