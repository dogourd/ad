package top.ezttf.ad.client.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * @author yuwen
 * @date 2019/1/24
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AdPlan {

    private Long id;
    private Long userId;
    private String planName;
    private Integer planStatus;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
