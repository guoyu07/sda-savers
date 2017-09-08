package com.sda.savers.web.exception;

import com.sda.savers.framework.common.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by Allen on 2017/8/28.
 */
@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public RestResult defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
        log.error("系统报错",e);
        return new RestResult(false,e.getMessage(),null,null);
    }

}
