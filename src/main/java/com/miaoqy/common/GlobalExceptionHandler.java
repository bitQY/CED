package com.miaoqy.common;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@RestControllerAdvice
//全局异常处理器
public class GlobalExceptionHandler {
    @ExceptionHandler(SQLIntegrityConstraintViolationException.class)
    public R<String> exceptionHandler(SQLIntegrityConstraintViolationException ex){
        log.info(ex.getMessage());
        //如果包含Duplicate entry，则说明有条目重复
        if (ex.getMessage().contains("Duplicate entry")){
            //对字符串切片用空格
            String[] split = ex.getMessage().split(" ");
            //字符串格式是固定的，所以这个位置必然是username,获取username的值
            String username = split[2];
            return R.error("用户名"+username+"已存在");
        }
        return R.error("网络异常（未知错误）");
    }


    @ExceptionHandler(CustomException.class)
    public R<String> exceptionHandler(CustomException ex){
        log.info(ex.getMessage());
        return R.error(ex.getMessage());
    }
}
