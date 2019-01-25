package top.ezttf.ad.index.adplan;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

/**
 * AdPlan 索引对象
 *
 * @author yuwen
 * @date 2019/1/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    private LocalDate startDate;
    private LocalDate endDate;

    public void update(AdPlanObject object) {
        if (object == null) {
            return;
        }
        if (object.getPlanId() != null) {
            this.planId = object.getPlanId();
        }
        if (object.getUserId() != null) {
            this.userId = object.getUserId();
        }
        if (object.getPlanStatus() != null) {
            this.planStatus = object.getPlanStatus();
        }
        if (object.getStartDate() != null) {
            this.startDate = object.getStartDate();
        }
        if (object.getEndDate() != null) {
            this.endDate = object.getEndDate();
        }
    }
}
