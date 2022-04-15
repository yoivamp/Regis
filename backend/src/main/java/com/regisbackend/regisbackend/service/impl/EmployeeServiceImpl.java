package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.common.EmployeeInit;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dao.EmployeeMapper;
import com.regisbackend.regisbackend.pojo.Employee;
import com.regisbackend.regisbackend.service.EmployeeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;

/**
 * @author 喵vamp
 */
@Service
@Slf4j
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {


    /**
     * 登录操作
     *
     * @param request  存储session
     * @param employee 员工信息
     * @return Result<Employee>
     */
    public Result<Employee> login(HttpServletRequest request, @RequestBody Employee employee) {
        //1、将页面提交的密码password进行md5加密处理
        String password = employee.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2、根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername, employee.getUsername());
        Employee emp = getOne(queryWrapper);
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
     * 新增员工
     *
     * @param request  获取创建者和更新者id
     * @param employee 新员工信息
     * @return 添加操作结果
     */
    public Result<String> save(HttpServletRequest request, @RequestBody Employee employee) {
        log.info("新增员工信息：{}", employee.toString());
        //员工信息初始化
        EmployeeInit.init(request, employee);
        //新增员工
        return save(employee) ? Result.success("添加员工成功") : null;
    }

    /**
     * 员工信息分页查询
     *
     * @param page     当前页
     * @param pageSize 每页条目数
     * @param name     查询员工姓名
     * @return 员工信息
     */
    public Result<Page<Employee>> getPage(int page, int pageSize, String name) {
        log.info("page={},pageSize={},name={}", page, pageSize, name);
        //构造分页构造器
        Page<Employee> pages = new Page<>(page, pageSize);
        //构造条件构造器
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        //添加过滤条件
        queryWrapper.like(Strings.isNotEmpty(name), Employee::getName, name);
        //添加排序条件
        queryWrapper.orderByDesc(Employee::getUpdateTime);
        //执行查询
        page(pages, queryWrapper);
        return Result.success(pages);
    }

    public Result<Employee> getEmployeeId(Long id) {
        log.info("根据id查询员工信息");
        //获取员工id
        Employee employee = getById(id);
        return employee != null ? Result.success(employee) : Result.error("没有查询到对应的员工信息");
    }


    /**
     * 更新员工信息
     *
     * @param request  获取更新时间和id
     * @param employee 更新的员工信息
     * @return 更新结果
     */
    public Result<String> updateEmployee(HttpServletRequest request, @RequestBody Employee employee) {
        log.info(employee.toString());
        //获取修改员工id
        Long emdId = (Long) request.getSession().getAttribute("employee");
        //设置更新时间和更新者
        employee.setUpdateTime(LocalDateTime.now());
        employee.setUpdateUser(emdId);
        return updateById(employee) ? Result.success("员工信息修改成功") : null;
    }
}
