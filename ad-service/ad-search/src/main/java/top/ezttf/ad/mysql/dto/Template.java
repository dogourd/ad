package top.ezttf.ad.mysql.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 对应resources目录下的template.json文件的java实体对象
 *
 * @author yuwen
 * @date 2019/1/30
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Template {

    private String databaseName;
    private List<JsonTable> tableList;
}
