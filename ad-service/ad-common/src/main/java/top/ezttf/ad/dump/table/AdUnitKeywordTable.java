package top.ezttf.ad.dump.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author yuwen
 * @date 2019/1/28
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdUnitKeywordTable {

    private Long unitId;
    private String keyword;
}
