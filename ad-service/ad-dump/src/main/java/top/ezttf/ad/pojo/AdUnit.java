package top.ezttf.ad.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdUnit {
    private Long id;

    private Long planId;

    private String unitName;

    private Integer unitStatus;

    private Integer positionType;

    private Long budget;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}