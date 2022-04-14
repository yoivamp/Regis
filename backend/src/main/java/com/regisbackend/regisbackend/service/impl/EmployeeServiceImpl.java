package com.regisbackend.regisbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.regisbackend.regisbackend.dao.EmployeeMapper;
import com.regisbackend.regisbackend.pojo.Employee;
import com.regisbackend.regisbackend.service.EmployeeService;
import org.springframework.stereotype.Service;

/**
 * @author 喵vamp
 */
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {
}
