package top.ezttf.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.ezttf.ad.mysql.constant.OpType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 投递的增量数据实体,
 *
 * @author yuwen
 * @date 2019/1/31
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MysqlRowData {

    private String tableName;

    private String level;

    private OpType opType;

    private List<Map<String, String>> fieldValueMapList = new ArrayList<>();
}
