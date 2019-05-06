package top.ezttf.ad.mysql.dto;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 之所以去监听binlog去构造增量数据是因为希望检索服务与投放系统之间去解耦,
 * 因为投放系统在工作中, 广告主可能对以前创造的投放数据进行增删改这些操作,
 * 而投放系统又不希望与检索系统之间产生关联。所以我们在检索系统中主动地去监听
 * MySQL的binlog然后解析得到增量数据实现更新。
 * 由于检索服务中没有去定义各个数据库以及数据表, 所以此处定义了一份模板文件
 * {@literal template.json} 通过解析模板文件得到想要解析的数据库以及数据表
 * 因为MySQL的binlog并不关心发生变化的数据库或数据表是哪一个(所有数据库表发生变化都会记录在binlog里面)。
 * 所以可以在模板文件中指明想要监听的那一部分是什么
 *
 * @author yuwen
 * @date 2019/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonTable {

    private String tableName;
    private Integer level;

    private List<Column> insertList;
    private List<Column> updateList;
    private List<Column> deleteList;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Column {
        @JSONField(name = "column")
        private String columnName;
    }
}
