package com.regisbackend.regisbackend.common;

/**
 * 自定义业务异常类
 * @author 喵vamp
 */
public class CustomException extends RuntimeException {
    public CustomException(String message){
        super(message);
    }
}
