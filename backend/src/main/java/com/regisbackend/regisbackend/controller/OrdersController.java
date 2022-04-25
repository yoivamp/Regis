package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.OrdersDto;
import com.regisbackend.regisbackend.pojo.Orders;
import com.regisbackend.regisbackend.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

/**
 * @author 喵vamp
 */
@RestController
@RequestMapping("/order")
public class OrdersController {
    @Autowired
    private OrdersService ordersService;

    /**
     * 用户下单
     *
     * @param orders 订单信息
     * @return 下单结果
     */
    @PostMapping("/submit")
    public Result<String> submit(@RequestBody Orders orders) {
        return ordersService.submitOrders(orders);
    }


    /**
     * 再来一单
     * @param orders 订单id
     * @return 跳转首页
     */
    @PostMapping("/again")
    public Result<String> againOrder(@RequestBody Orders orders) {
        return ordersService.againOrder(orders);
    }

    /**
     * 分页查询订单
     *
     * @param page     当前页码
     * @param pageSize 每页条目数
     * @return 查询page
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> page(int page, int pageSize) {
        return ordersService.pageOrderDetail(page, pageSize);
    }

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
    @GetMapping("/page")
    public Result<Page<OrdersDto>> pageBackend(int page, int pageSize, String number, LocalDateTime orderTime, LocalDateTime endTime) {
        return ordersService.pageBackend(page, pageSize, number, orderTime, endTime);
    }

    /**
     * 派送订单
     *
     * @param orders 订单id和状态
     * @return 派送结果
     */
    @PutMapping
    public Result<String> send(@RequestBody Orders orders) {
        return ordersService.sendOrder(orders);
    }
}
