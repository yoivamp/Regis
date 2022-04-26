package com.regisbackend.regisbackend.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.OrdersDto;
import com.regisbackend.regisbackend.pojo.Orders;
import org.springframework.web.bind.annotation.RequestBody;


import java.time.LocalDateTime;

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
     * 再来一单
     *
     * @param orders 订单id
     * @return 跳转首页
     */
    Result<String> againOrder(Orders orders);

    /**
     * 分页查询订单
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 查询page
     */
    Result<Page<OrdersDto>> pageOrderDetail(int page, int pageSize);

    /**
     * 后端管理订单的分页及条件查询
     *
     * @param page      当前页码
     * @param pageSize  每页条目数
     * @param number    订单号
     * @param orderTime 下单时间
     * @param endTime   截止时间
     * @return 查询结果
     */
    Result<Page<OrdersDto>> pageBackend(int page, int pageSize, String number, LocalDateTime orderTime, LocalDateTime endTime);


    /**
     * 派送订单
     *
     * @param orders 订单id和状态
     * @return 派送结果
     */
    Result<String> sendOrder(Orders orders);
}
