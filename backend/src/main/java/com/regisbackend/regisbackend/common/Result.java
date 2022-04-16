package com.regisbackend.regisbackend.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * 服务端统一返回结果类
 * @author 喵vamp
 */
@Data
public class Result<T> {
    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 提示信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    private Map map = new HashMap();

    public static <T> Result<T> success(T object) {
        Result<T> result = new Result<>();
        result.data = object;
        result.code =200;
        return result;
    }

    public static <T> Result error(String message) {
        Result<T> result = new Result<>();
        result.code=199;
        result.message=message;
        return result;
    }

    public Result<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
