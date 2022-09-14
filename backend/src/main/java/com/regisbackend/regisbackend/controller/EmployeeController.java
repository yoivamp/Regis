package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.Employee;
import com.regisbackend.regisbackend.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @author 喵vamp
 * @Description 员工管理
 */
@RestController
@Slf4j
@RequestMapping("/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService empService;

    /**
     * 登录操作
     *
     * @param employee 员工信息
     * @return Result<Employee>
     */
    @PostMapping("/login")
    public Result<Employee> login(@RequestBody Employee employee) {
        return empService.login(employee);
    }

    /**
     * 退出登录
     *
     * @return Result<String>
     */
    @PostMapping("/logout")
    public Result<String> logout() {
        //清理Session中保存的当前登录员工的id
        //request.getSession().removeAttribute("employee");
        return empService.logout();
    }

    /**
     * 新增员工
     *
     * @param employee 新员工信息
     * @return 添加操作结果
     */
    @PostMapping
    public Result<String> save(@RequestBody Employee employee) {
        return empService.saveEmployee(employee);
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
        return empService.getPage(page, pageSize, name);
    }

    /**
     * 根据员工id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    @GetMapping("/{id}")
    public Result<Employee> getById(@PathVariable("id") Long id) {
        return empService.getEmployeeId(id);
    }

    /**
     * 更新员工信息
     *
     * @param employee 更新的员工信息
     * @return 更新结果
     */
    @PutMapping
    public Result<String> update(@RequestBody Employee employee) {
        return empService.updateEmployee(employee);
    }

}
