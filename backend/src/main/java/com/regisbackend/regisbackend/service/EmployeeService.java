package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.Employee;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author 喵vamp
 */
public interface EmployeeService extends IService<Employee> {
    /**
     * 登录操作
     *
     * @param employee 员工信息
     * @return Result<Employee>
     */
    Result<Employee> login(Employee employee);

    /**
     * 新增员工
     *
     * @param employee 新员工信息
     * @return 添加操作结果
     */
    Result<String> saveEmployee(Employee employee);

    /**
     * 员工信息分页查询
     *
     * @param page     当前页
     * @param pageSize 每页条目数
     * @param name     查询员工姓名
     * @return 员工信息
     */
    Result<Page<Employee>> getPage(int page, int pageSize, String name);

    /**
     * 根据员工id查询员工信息
     *
     * @param id 员工id
     * @return 员工信息
     */
    Result<Employee> getEmployeeId(Long id);

    /**
     * 更新员工信息
     *
     * @param employee 更新的员工信息
     * @return 更新结果
     */
    @Transactional
    Result<String> updateEmployee(Employee employee);

    /**
     * 退出登录
     *
     * @return 移除redis存储的key
     */
    Result<String> logout();
}
