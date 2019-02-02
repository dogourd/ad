package top.ezttf.ad.search.vo.feature;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 关键词匹配信息
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeywordFeature {

    private List<String> keywords;
}
