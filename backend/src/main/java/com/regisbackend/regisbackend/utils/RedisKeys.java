package com.regisbackend.regisbackend.utils;

/**
 * @Description TODO
 * @Author 喵vamp
 * @Date 2022/9/6 11:46
 */
public class RedisKeys {
    public static final String EMPLOYEE_LOGIN_KEY = "Employee:login";

    public static final Long EMPLOYEE_LOGIN_TTL = 30L;

    public static final String USER_LOGIN_CODE_KEY = "User:login:code:";
    public static final String USER_LOGINED_KEY = "User:logined";
    public static final Long USER_LOGIN_CODE_TTL = 5L;
    public static final Long USER_LOGINED_TTL = 30L;
}
