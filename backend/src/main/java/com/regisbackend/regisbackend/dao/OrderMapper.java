package com.regisbackend.regisbackend.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.regisbackend.regisbackend.pojo.Orders;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 喵vamp
 */
@Mapper
public interface OrderMapper extends BaseMapper<Orders> {
}
