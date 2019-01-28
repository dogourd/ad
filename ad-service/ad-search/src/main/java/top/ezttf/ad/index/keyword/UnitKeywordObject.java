package top.ezttf.ad.index.keyword;

import lombok.*;

/**
 * 关键词索引对象
 *
 * @author yuwen
 * @date 2019/1/26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UnitKeywordObject {

    private Long unitId;
    private String keyword;

}
