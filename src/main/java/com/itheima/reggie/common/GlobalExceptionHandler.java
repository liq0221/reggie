package com.itheima.reggie.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

/**
 * @Description: test
 * @Create by: 草滩彭于晏
 * @Date:2021/10/31 15:39
 */
@RestController
@ControllerAdvice(annotations = {RestController.class, Controller.class})
@Slf4j
public class GlobalExceptionHandler {
    /**
     * 异常处理方法
     *
     * @return
     */
    @ExceptionHandler({Exception.class})
    public R<String> exceptionHandler(Exception e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error("服务器错误");
    }

    @ExceptionHandler({CommonException.class})
    public R<String> exceptionHandler(CommonException e) {
        e.printStackTrace();
        log.error(e.getMessage());
        return R.error(e.getMessage());
    }


}
