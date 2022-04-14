package com.regisbackend.regisbackend.common;

import lombok.Data;
import org.springframework.util.AntPathMatcher;

import java.util.Arrays;
import java.util.List;

/**
 * @author 喵vamp
 */
@Data
public class PathLocation {
    /**
     * 路径匹配器，支持通配符
     */
    public static final AntPathMatcher PATH_MATCHER ;

    public static List<String> strings;
    static {
        PATH_MATCHER= new AntPathMatcher();
        strings = Arrays.asList("/employee/login",
                "/employee/logout",
                "/backend/**",
                "/front/**");
    }
}
