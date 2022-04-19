package com.regisbackend.regisbackend.common;

import com.regisbackend.regisbackend.pojo.Employee;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;

/**
 * 新增员工信息初始化类
 *
 * @author 喵vamp
 */
public class EmployeeInit {

    public static void init(Employee employee) {
        //设置初始密码123456，使用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
    }
}
