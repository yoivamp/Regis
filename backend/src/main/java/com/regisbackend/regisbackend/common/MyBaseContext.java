package com.regisbackend.regisbackend.common;

/**
 * 基于ThreadLocal封装工具类，用户保存和获取当前登录用户id
 *
 * @author 喵vamp
 */
public class MyBaseContext {
    /**
     * 当前线程的局部变量
     */
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    /**
     * 设置当前线程操作员工id
     *
     * @param id 操作员工id
     */
    public static void setCurrentId(Long id) {
        threadLocal.set(id);
    }

    /**
     * 获取当前线程操作员工id
     *
     * @return 操作员工id
     */
    public static Long getCurrentId() {
        return threadLocal.get();
    }
}
