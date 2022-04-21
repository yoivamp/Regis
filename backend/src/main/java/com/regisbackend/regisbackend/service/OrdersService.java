package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.OrdersDto;
import com.regisbackend.regisbackend.pojo.Orders;

/**
 * @author 喵vamp
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param orders 订单信息
     * @return 下单结果
     */
    Result<String> submitOrders(Orders orders);

    /**
     * 分页查询订单
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 查询page
     */
    Result<Page<OrdersDto>> pageOrderDetail(int page, int pageSize);
}
