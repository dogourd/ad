package top.ezttf.ad.index.adplan;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDateTime;

/**
 * AdPlan 索引对象(推广计划)
 *
 * @author yuwen
 * @date 2019/1/25
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class AdPlanObject {

    private Long planId;
    private Long userId;
    private Integer planStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startDate;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endDate;

    public AdPlanObject update(AdPlanObject object) {
        if (object == null) {
            return this;
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
        return this;
    }
}
