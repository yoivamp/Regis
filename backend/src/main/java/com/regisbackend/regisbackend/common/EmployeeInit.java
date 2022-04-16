package com.regisbackend.regisbackend.common;

import com.regisbackend.regisbackend.pojo.Employee;
import org.springframework.util.DigestUtils;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author 喵vamp
 */
public class EmployeeInit {

    public static void  init(HttpServletRequest request, Employee employee) {
        //设置初始密码123456，使用MD5加密
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));
        //设置创建和更新时间
        employee.setCreateTime(LocalDateTime.now());
        employee.setUpdateTime(LocalDateTime.now());
        //设置创建者和更新者id
        Long emdId = (Long) request.getSession().getAttribute("employee");
        employee.setCreateUser(emdId);
        employee.setUpdateUser(emdId);
    }
}
