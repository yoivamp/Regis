package com.regisbackend.regisbackend.common;

import lombok.Data;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * 必要访问路径类
 *
 * @author 喵vamp
 */
@Data
public class PathLocation {
    /**
     * 路径匹配器，支持通配符
     */
    public static final AntPathMatcher PATH_MATCHER;
    /**
     * 必要访问路径集合
     */
    public static List<String> strings;

    static {
        PATH_MATCHER = new AntPathMatcher();
        strings = Arrays.asList("/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**",
                "/common/**",
                "/user/sendMsg",
                "/user/login",
                "/doc.html",
                "/webjars/**",
                "/swagger-resources",
                "/v2/api-docs");
    }
}
