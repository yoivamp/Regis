package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.Employee;
import com.regisbackend.regisbackend.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author 喵vamp
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {
    /**
     * employee的service的impl
     */
    @Autowired
    private EmployeeService employeeService;

    /**
     * 登录操作
     * @param request 存储session
     * @param employee 员工信息
     * @return Result<Employee>
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = employeeService.getOne(queryWrapper);
        //3、如果没有查询到则返回登录失败结果
        if (emp == null) {
            return Result.error("账号不存在");
        }
        //4、密码比对，如果不一致则返回登录失败结果
        if (!emp.getPassword().equals(password)) {
            return Result.error("密码错误");
        }
        //5、查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0) {
            return Result.error("账号已禁用");
        }
        //6、登录成功，将员工id存入Session并返回登录成功结果
        request.getSession().setAttribute("employee", emp.getId());
        return Result.success(emp);
    }

    /**
     * 退出登录
     * @param request 清除session
     * @return Result<String>
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request){
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }
}
