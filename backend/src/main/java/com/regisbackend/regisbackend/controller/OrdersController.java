package com.regisbackend.regisbackend.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.regisbackend.regisbackend.common.Result;
import com.regisbackend.regisbackend.dto.OrdersDto;
import com.regisbackend.regisbackend.pojo.Orders;
import com.regisbackend.regisbackend.service.OrdersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
     * 分页查询订单
     * @param page 当前页码
     * @param pageSize 每页条目数
     * @return 查询page
     */
    @GetMapping("/userPage")
    public Result<Page<OrdersDto>> page(int page, int pageSize){
        return ordersService.pageOrderDetail(page,pageSize);
    }
}
