package top.ezttf.ad.client.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
