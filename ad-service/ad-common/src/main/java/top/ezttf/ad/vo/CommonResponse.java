package top.ezttf.ad.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author yuwen
 * @date 2019/1/19
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonResponse<T> {

    private Integer code;
    private String message;
    private T data;

    public CommonResponse(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
}
