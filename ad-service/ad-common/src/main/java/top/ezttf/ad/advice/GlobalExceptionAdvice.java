package top.ezttf.ad.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import top.ezttf.ad.enums.ResponseCode;
import top.ezttf.ad.exception.AdException;
import top.ezttf.ad.vo.CommonResponse;

import javax.servlet.http.HttpServletRequest;

/**
 * @author yuwen
 * @date 2019/1/19
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = {AdException.class})
    public CommonResponse handleAdException(HttpServletRequest request, AdException e) {
        CommonResponse<Object> commonResponse = new CommonResponse<>(
                ResponseCode.ERROR.getCode(),
                "business error"
        );
        commonResponse.setData(e.getMessage());
        log.error(request.getRequestURI() + " error, " + e);
        return commonResponse;

    }
}
