package top.ezttf.ad.mysql;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import top.ezttf.ad.mysql.constant.OpType;
import top.ezttf.ad.mysql.dto.ParseTemplate;
import top.ezttf.ad.mysql.dto.TableTemplate;
import top.ezttf.ad.mysql.dto.Template;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * @author yuwen
 * @date 2019/1/31
 */
@Slf4j
@Component
public class TemplateHolder {

    private ParseTemplate parseTemplate;

    private final JdbcTemplate jdbcTemplate;


    private final String SQL = "" +
            " SELECT table_schema, table_name, column_name, ordinal_position" +
            " FROM information_schema.columns" +
            " WHERE table_schema = ?" +
            " AND table_name = ?";

    @Autowired
    public TemplateHolder(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    /**
     * 容器启动 加载 {@link TemplateHolder} 后即将json解析初始化 构造{@link ParseTemplate}
     */
    @PostConstruct
    private void init() {
        loadJson("template.json");
    }

    /**
     * 提供对外服务, 解析 binlog 对应的每一张表
     *
     * @param tableName 表名
     */
    public TableTemplate getTable(String tableName) {
        return parseTemplate.getTableTemplateMap().get(tableName);
    }

    private void loadJson(String path) {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(path)) {
            Template template = JSON.parseObject(inputStream, StandardCharsets.UTF_8, Template.class);
            this.parseTemplate = ParseTemplate.parse(template);
            loadMeta();
        } catch (IOException e) {
            log.error(e.getMessage());
            throw new RuntimeException("failed to parse json file");
        }

    }

    /**
     * 加载数据表元信息, 向{@link ParseTemplate} 中封装列索引和列名的映射信息
     */
    private void loadMeta() {
        parseTemplate.getTableTemplateMap().forEach((key, tableTemplate) -> {
            List<String> updateFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.UPDATE);
            List<String> insertFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.ADD);
            List<String> deleteFields = tableTemplate.getOpTypeFieldSetMap().get(OpType.DELETE);
            jdbcTemplate.query(SQL,
                    new Object[]{parseTemplate.getDatabaseName(), tableTemplate.getTableName()},
                    (resultSet, i) -> {
                        int position = resultSet.getInt("ordinal_position");
                        String columnName = resultSet.getString("column_name");
                        if ((updateFields != null && updateFields.contains(columnName)) ||
                                (insertFields != null && insertFields.contains(columnName)) ||
                                (deleteFields != null && deleteFields.contains(columnName))) {
                            // 查询语句查询出来索引是从 1 开始, binlog-connector-java的索引是从0开始, 所以需要position - 1
                            tableTemplate.getPositionMap().put(position - 1, columnName);
                        }
                        return null;
                    });
        });
    }
}
