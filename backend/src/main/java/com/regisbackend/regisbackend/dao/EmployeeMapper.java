package com.regisbackend.regisbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regisbackend.regisbackend.pojo.Employee;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 喵vamp
 */
@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
