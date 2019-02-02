package top.ezttf.ad.search.vo.media;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 终端信息, 即应用的基本信息
 *
 * @author yuwen
 * @date 2019/2/1
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class App {

    /**
     * App 编码
     */
    private String appCode;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 应用的包名
     */
    private String packageName;

    /**
     * 应用请求页面名
     */
    private String activityName;


}
