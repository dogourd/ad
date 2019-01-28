package top.ezttf.ad.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.ezttf.ad.constant.CommonStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * Created by Qinyi.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "ad_unit")
public class AdUnit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "plan_id", nullable = false)
    private Long planId;

    @Basic
    @Column(name = "unit_name", nullable = false)
    private String unitName;

    @Basic
    @Column(name = "unit_status", nullable = false)
    private Integer unitStatus;

    /** 广告位类型(开屏, 贴片, 中贴...) */
    @Basic
    @Column(name = "position_type", nullable = false)
    private Integer positionType;

    @Basic
    @Column(name = "budget", nullable = false)
    private Long budget;

    @Basic
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Basic
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public AdUnit(Long planId, String unitName,
                  Integer positionType, Long budget) {
        this.planId = planId;
        this.unitName = unitName;
        this.unitStatus = CommonStatus.VALID.getStatus();
        this.positionType = positionType;
        this.budget = budget;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }
}
