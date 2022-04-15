package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.Employee;
import com.regisbackend.regisbackend.service.impl.EmployeeServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    private EmployeeServiceImpl empServiceImpl;

    /**
     * 登录操作
     *
     * @param request  存储session
     * @param employee 员工信息
     * @return Result<Employee>
     */
    @PostMapping("/login")
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        return empServiceImpl.login(request, employee);
    }

    /**
     * 退出登录
     *
     * @param request 清除session
     * @return Result<String>
     */
    @PostMapping("/logout")
    public Result<String> logout(HttpServletRequest request) {
        //清理Session中保存的当前登录员工的id
        request.getSession().removeAttribute("employee");
        return Result.success("退出成功");
    }

    /**
     * 新增员工
     *
     * @param request  获取创建者和更新者id
     * @param employee 新员工信息
     * @return 添加操作结果
     */
    @PostMapping
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        return empServiceImpl.save(request, employee);
    }

    /**
     * 员工信息分页查询
     *
     * @param page     当前页
     * @param pageSize 每页条目数
     * @param name     查询员工姓名
     * @return 员工信息
     */
    @GetMapping("/page")
    public Result<Page<Employee>> page(int page, int pageSize, String name) {
        return empServiceImpl.getPage(page, pageSize, name);
    }

    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable("id") Long id) {
        return empServiceImpl.getEmployeeId(id);
    }

    /**
     * 更新员工信息
     *
     * @param request  获取更新时间和id
     * @param employee 更新的员工信息
     * @return 更新结果
     */
    @PutMapping
    public Result<String> update(HttpServletRequest request, @RequestBody Employee employee) {
        return empServiceImpl.updateEmployee(request, employee);
    }

}
