package top.ezttf.ad.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
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