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
@Table(name = "ad_plan")
public class AdPlan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Basic
    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Basic
    @Column(name = "plan_name", nullable = false)
    private String planName;

    @Basic
    @Column(name = "plan_status", nullable = false)
    private Integer planStatus;

    @Basic
    @Column(name = "start_date", nullable = false)
    private LocalDateTime startDate;

    @Basic
    @Column(name = "end_date", nullable = false)
    private LocalDateTime endDate;

    @Basic
    @Column(name = "create_time", nullable = false)
    private LocalDateTime createTime;

    @Basic
    @Column(name = "update_time", nullable = false)
    private LocalDateTime updateTime;

    public AdPlan(Long userId, String planName,
                  LocalDateTime startDate, LocalDateTime endDate) {

        this.userId = userId;
        this.planName = planName;
        this.planStatus = CommonStatus.VALID.getStatus();
        this.startDate = startDate;
        this.endDate = endDate;
        this.createTime = LocalDateTime.now();
        this.updateTime = this.createTime;
    }
}
