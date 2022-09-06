package com.regisbackend.regisbackend.utils;

/**
 * @author 喵vamp
 * @Description 基于ThreadLocal封装工具类，保存员工和获取当前登录员工信息
 */
public class EmployeeHolder {
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
