package top.ezttf.ad.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdCreative {
    private Long id;

    private String name;

    private Byte type;

    private Byte materialType;

    private Integer height;

    private Integer width;

    private Long size;

    private Integer duration;

    private Byte auditStatus;

    private Long userId;

    private String url;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

}