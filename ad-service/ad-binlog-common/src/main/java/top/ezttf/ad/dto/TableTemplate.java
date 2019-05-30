package top.ezttf.ad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.ezttf.ad.constant.OpType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 方便操作时读取一些表信息
 *
 * @author yuwen
 * @date 2019/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TableTemplate {

    private String tableName;

    private String level;

    /**
     * 操作类型 ---> 列
     */
    private Map<OpType, List<String>> opTypeFieldSetMap = new HashMap<>(1 << 4);

    /**
     * 字段索引 ---> 字段名映射
     * 因为监听到的日志中不能直接获取到字段名而是 0, 1, 2, 3使用该Map将其对应到列名上
     */
    private Map<Integer, String> positionMap = new HashMap<>(1 << 4);
}
