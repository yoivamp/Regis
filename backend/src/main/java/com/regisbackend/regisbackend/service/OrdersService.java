package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.pojo.OrderDetail;
import com.regisbackend.regisbackend.pojo.Orders;

import java.util.List;

/**
 * @author 喵vamp
 */
public interface OrdersService extends IService<Orders> {

    /**
     * 用户下单
     *
     * @param  orders 订单信息
     * @return 下单结果
     */
    Result<String> submitOrders(Orders orders);

    Result<Page<OrderDetail>> pageOrderDetail(int page, int pageSize);
}
